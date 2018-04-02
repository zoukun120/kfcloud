package com.zk.kfcloud.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zk.kfcloud.Exception.AccessException;
import com.zk.kfcloud.Utils.JsonResult;
import com.zk.kfcloud.Utils.wechat.AccessGuide;
import com.zk.kfcloud.Utils.wechat.Authorization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RestController
public class WeChatController {

    @GetMapping("/access")
    public static JsonResult access(HttpServletRequest request, HttpServletResponse response) {
        try {
            AccessGuide.doGet(request, response);
            return JsonResult.ok("Access to wechat server successfully");
        } catch (Exception e) {
            new AccessException("接入微信服务器异常！");
            return JsonResult.errMsg("接入微信服务器异常!");
        }
    }

    @GetMapping("/code")
    public static JsonResult login(HttpServletRequest request, HttpServletResponse response) {
        try {
            String s = Authorization.CodeUrl();
            log.info("CodeUrl:"+s);
            response.sendRedirect(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JsonResult.ok("User Login Successfully");
    }

    @GetMapping("/redirect_uri")
    public static JsonResult redirect(HttpServletRequest request) {
        try{
//            获取code
            String code = request.getParameter("code");
//            用code换access_token
            String tokenAndOpenId = Authorization.getTokenAndOpenId(code);
//            通过access_token和openid拉取用户信息
            JSONObject tokenJObj = JSON.parseObject(tokenAndOpenId);
            String userInfo = Authorization.getUserInfo(tokenJObj.getString("openid"), tokenJObj.getString("access_token"));
//            日志
            log.info("code:"+code);
            log.info(tokenAndOpenId);
            log.info("userInfo:"+userInfo);
            return JsonResult.ok("User Authorization Successfully");
        }catch (Exception e){
            new AccessException("用户授权失败！");
            return JsonResult.errMsg("用户授权失败！");
        }

    }
}
