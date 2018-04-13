package com.zk.kfcloud.Controller;

import com.zk.kfcloud.Dao.WeChatMapper;
import com.zk.kfcloud.Entity.web.*;
import com.zk.kfcloud.Exception.UserNotFoundException;
import com.zk.kfcloud.Service.MenuService;
import com.zk.kfcloud.Service.UserService;
import com.zk.kfcloud.Utils.Tools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.List;


@Slf4j
@Controller
public class LoginController {

    @Autowired
    private UserService userService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private WeChatMapper weChatMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    RedisTemplate<String,User> userRedisTemplate;
    /**
     * 一切请求从get方式的login起(用户点击‘空分云’按钮)，然后再接入微信api，获取到openid传到login.html
     * @return
     */
    @GetMapping(value = {"/","/login"})
    public void login(HttpServletResponse response) throws IOException {
        response.sendRedirect("/code");
    }

    /**
     * 只有新微信用户才会登陆,登陆成功后，将userid和openid保存在tb2_wechat表中,以实现多微信用户登陆
     * @param username
     * @param password
     * @param openid
     * @param model
     * @param session
     * @return
     * @throws UserNotFoundException
     */
    @PostMapping("/index")
    public String toIndex(String username, String password, String openid, Model model, HttpSession session) throws UserNotFoundException {
//        1.将参数openid返回给login和index页面
        model.addAttribute("openid",openid);
        log.info("username:"+username+",password:"+password+",openid:"+openid);
        log.info("username:"+username+",password:"+Tools.md5(password)+",openid:"+openid);
//        2.获取登陆表单提交的三个参数，并查询数据库，
        Integer userid = userService.selectByNameAndPwd(new User(username,Tools.md5(password)));
        System.err.println("userid:"+userid);
//        3.判断数据库中是否存在该账号（0表示不存在）
        if(userid!=0){
            User user = userService.selectByPrimaryKey(userid);
//           3.1 登陆成功，改变status、最后登录时间、记录session(sessonid和当前登陆用户)
            user.setStatus(1);
            user.setLastLogin(new Date());
            userService.updateUserLoginStatus(user);
            userService.updateLastLogin(user);
            stringRedisTemplate.opsForValue().set("JSESSIONID",session.getId());
            userRedisTemplate.opsForValue().set("SESSIONUSER",user);
            log.info("当前用户："+user+",已添加 sessionId 和 User");
//            3.2 一个空分账号可以绑定多个微信号（openid不同），如果是新微信号，则在tb2_wechat表中插入openid和userid信息
            List<WeChat> allWeChatUser = weChatMapper.findAllWeChatUser();
            Boolean flag = true;
            for (WeChat wxUser:allWeChatUser) {
                log.info(wxUser.toString());
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
            return "index";
        }else {
           return "login";
        }
    }

    @GetMapping("index")
    public String toTestIndex( ){
        return "index";
    }

    @GetMapping("index2")
    public String toTestIndex2( ){
        return "index";
    }

}
