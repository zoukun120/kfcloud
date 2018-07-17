package com.zk.kfcloud.Config.Listener;

import com.zk.kfcloud.Service.UserService;
import com.zk.kfcloud.Service.WeChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.*;

@Slf4j
@WebListener
public class StatusSessionListener implements HttpSessionListener  {


    @Autowired
    private UserService userService;

    @Autowired
    private WeChatService weChatService;

    @Override
    public void sessionCreated(HttpSessionEvent se) {
//        String openid = String.valueOf(se.getSession().getAttribute("openid"));
        log.info("sessionCreated");
    }

    /**
     * session销毁时，将微信用户的登陆状态改为0
     * @param se
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        String openid = String.valueOf(se.getSession().getAttribute("openid"));
        log.info("已把[openid="+openid+"]从session中剔除");
//        System.err.println("已把[openid="+openid+"]从session中剔除");
        weChatService.updateLoginStatus(openid,false);
    }
}
