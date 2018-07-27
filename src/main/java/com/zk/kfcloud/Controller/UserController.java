package com.zk.kfcloud.Controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zk.kfcloud.Entity.web.User;
import com.zk.kfcloud.Entity.web.WebWeChat;
import com.zk.kfcloud.Service.UserService;
import com.zk.kfcloud.Service.WeChatService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.zk.kfcloud.Service.FactoryService;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private WeChatService weChatService;

    @Autowired
    private FactoryService factoryService;
    /**
     * 根据openid的值判断该微信用户是否为自己人，
     * 如果是就将改用户session记录起来，再根据权限，展现相应的index内容
     * 如果不是，重定向到login页面，迫使用户输入账号和密码，并将改用户的openid保存到user对象中
     *
     * @param openid
     * @return
     */
    @GetMapping("/isBrother")
    public String Brother(@RequestParam("openid") String openid,@RequestParam("access_token") String access_token, Model model) {
        User brother = userService.isBrother(openid);
        log.info("access_token:" + access_token);
       log.info("brother:" + brother);
        if (brother != null) {
            log.info("内部用户，直接进入系统");
            log.info("重定向url：redirect:index?userid=" + brother.getUserId()+"&openid="+openid+"&access_token="+access_token);
            return "redirect:index?userid=" + brother.getUserId()+"&openid="+openid;
        } else {
            //将参数openid返回给login页面
            model.addAttribute("openid", openid);
            model.addAttribute("access_token", access_token);
            log.info("新用户，请进入登陆页");
            return "login";
        }
    }

    /**
     * 用户管理页面，分页查询
     * @param page
     * @param limit
     * @return
     */
    @GetMapping("/users")
    @ResponseBody
    public Map<String, Object> pageUser(Integer page,Integer limit) {
        Page<WebWeChat> users = PageHelper.startPage(page, limit).doSelectPage(
                () -> {
                    weChatService.findAllUser();
                }
        );
        Map<String, Object> res = new HashMap<>();
        res.put("code", 0);
        res.put("msg", "");
        res.put("count", weChatService.findAllUser().size());
        res.put("data", JSONArray.fromObject(users));
        return res;
    }

   /*
   向settings页面传openid和开关状态，并打开settings页面
    */
    @GetMapping("/settings/{openid}")
    public String sendPage( @PathVariable("openid") String openid, Model model) {
        model.addAttribute("openid", openid);   //将openid保存settings页面中
        model.addAttribute("stateValue", factoryService.AlarmIndex(openid));
        //从openid获取报警时间段
        String alarmtimeon=factoryService.getAlarmtimeonByopenId(openid);
            if(alarmtimeon==null)  {alarmtimeon="未设置";}
        model.addAttribute("alarmtimeon", alarmtimeon);
        return "settings";
    }
    //接收前端报警开关的请求存入数据库
    @PostMapping("/state")
    public @ResponseBody Map<String, Object> insetalarm_auth(@RequestBody Map<String, Object> map) {
        Boolean state = (Boolean) map.get("state");
        String Openid = String.valueOf(map.get("openid"));
        weChatService.updatestateByopenId(state, Openid); //将值写入数据库
        return map;
    }

    //接收前端报警开关的请求存入数据库
    @PostMapping("/alarmtimeon")
    public @ResponseBody Map<String, Object> insetalarmtimeon(@RequestBody Map<String, Object> map) {
        String alarmtimeon = String.valueOf(map.get("alarmtimeon"));
        String Openid = String.valueOf(map.get("openid"));
        log.info("更改报警时间段："+alarmtimeon+";    openid:"+Openid);
        weChatService.updateAlarmtimeonByopenId(alarmtimeon, Openid); //将报警时间段值写入数据库
        return map;
    }

}
