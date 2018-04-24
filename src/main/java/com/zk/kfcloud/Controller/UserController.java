package com.zk.kfcloud.Controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.gson.JsonObject;
import com.zk.kfcloud.Dao.WeChatMapper;
import com.zk.kfcloud.Entity.web.Role;
import com.zk.kfcloud.Entity.web.User;
import com.zk.kfcloud.Service.UserService;
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
    private WeChatMapper weChatMapper;

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
        System.err.println("brother:" + brother);
        if (brother != null) {
            System.err.println("自己人，马上进入首页");
//            session.setAttribute("sessionUser", brother);
            System.err.println("redirect:index?userid=" + brother.getUserId());
            return "redirect:index?userid=" + brother.getUserId();
        } else {
            model.addAttribute("openid", openid);
            System.err.println("新用户，请进入登陆页");
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
        Page<User> users = PageHelper.startPage(page, limit).doSelectPage(
                () -> {
                    userService.findAllUsers();
                }
        );
        for (User user : users) {
            log.info("userId = " + user.getUserId());
            if (user.getRole()==null){
                Role role = new Role();
                role.setRoleId(1);
                role.setRights("123456");
                role.setRoleName("规避掉空值");
                user.setRole(role);
            }
        }
        Map<String, Object> res = new HashMap<>();
        System.err.println(users.size());
        res.put("code", 0);
        res.put("msg", "");
        res.put("count", userService.findAllUsers().size());
        res.put("data", JSONArray.fromObject(users));
        return res;
    }
}
