package com.zk.kfcloud.Controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zk.kfcloud.Entity.web.Role;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private WeChatService weChatService;

    /**
     * 根据openid的值判断该微信用户是否为自己人，
     * 如果是就将改用户session记录起来，再根据权限，展现相应的index内容
     * 如果不是，重定向到login页面，迫使用户输入账号和密码，并将改用户的openid保存到user对象中
     *
     * @param openid
     * @return
     */
    @GetMapping("/isBrother")
    public String Brother(@RequestParam("openid") String openid, Model model) {
        User brother = userService.isBrother(openid);
       log.info("brother:" + brother);
        if (brother != null) {
            log.info("内部用户，直接进入系统");
            log.info("重定向url：redirect:index?userid=" + brother.getUserId());
            return "redirect:index?userid=" + brother.getUserId()+"&openid="+openid;
        } else {
            model.addAttribute("openid", openid);
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
}
