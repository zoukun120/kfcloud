package com.zk.kfcloud.Controller;

import com.zk.kfcloud.Entity.web.User;
import com.zk.kfcloud.Service.UserService;
import com.zk.kfcloud.Utils.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 根据openid的值判断该微信用户是否为自己人，
     * 如果是就将改用户session记录起来，再根据权限，展现相应的index内容
     * 如果不是，重定向到login页面，迫使用户输入账号和密码，并将改用户的openid保存到user对象中
     * @param openid
     * @return
     */
    @GetMapping("/isBrother")
    public JsonResult Brother(@RequestParam("openid") String openid){
        System.err.println(openid);
        User brother = userService.isBrother(openid);
        System.err.println(brother);
        return  JsonResult.ok(brother);
    }
}
