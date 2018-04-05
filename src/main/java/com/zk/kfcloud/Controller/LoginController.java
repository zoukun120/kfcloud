package com.zk.kfcloud.Controller;

import com.zk.kfcloud.Dao.WeChatMapper;
import com.zk.kfcloud.Entity.web.Form;
import com.zk.kfcloud.Entity.web.User;
import com.zk.kfcloud.Entity.web.WeChat;
import com.zk.kfcloud.Service.RedisService;
import com.zk.kfcloud.Service.UserService;
import com.zk.kfcloud.Utils.Tools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;


@Slf4j
@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private WeChatMapper weChatMapper;

    @Autowired
    private RedisService redisService;
    /*
        只有新微信用户才会登陆
        登陆成功后，将userid和openid保存在tb2_wechat表中
        以实现多微信用户登陆。
     */
    @PostMapping("/login")
    public String login(String username,String password,String openid, HttpSession session, HttpServletResponse response) throws IOException {
        System.err.println("username:"+username);
        System.err.println("password:"+Tools.md5(password));
        System.err.println("openid:"+openid);
        User u = new User();
        u.setLoginname(username);
        u.setPassword(Tools.md5(password));
        Integer userid = userService.selectByNameAndPwd(u);
        System.err.println("userid:"+userid);
        if(userid!=0){//表示该用户登陆成功
            //记录改用户的的session
            redisService.set("sessionid",session.getId());
            redisService.lpush("userid",userid);
            log.info("欢迎来到登陆页面:"+session.getId());
            //在tb2_wechat表中插入openid和userid信息
            List<WeChat> allWeChatUser = weChatMapper.findAllWeChatUser();
            Boolean flag = true;
            System.err.println(openid);
            for (WeChat wxUser:allWeChatUser) {
                System.err.println(wxUser);
                if (openid.equals(wxUser.getOpenId())) {//openid存在，不插入数据库
                    flag = false;
                }
            }
            if (flag){
                WeChat wx = new WeChat();
                wx.setUserId(userid);
                wx.setOpenId(openid);
                weChatMapper.insert(wx);
            }
            return "/index";
        }else {
           return "/login";
        }
    }
}
