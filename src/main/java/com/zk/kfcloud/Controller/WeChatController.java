package com.zk.kfcloud.Controller;

import com.zk.kfcloud.Exception.AccessException;
import com.zk.kfcloud.Exception.MenuCreateException;
import com.zk.kfcloud.Utils.JsonResult;
import com.zk.kfcloud.Utils.wechat.AccessGuide;
import com.zk.kfcloud.Utils.wechat.Authorization;
import com.zk.kfcloud.Utils.wechat.CustomerMenu;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 调试微信步骤：
 * 1. 打开外网映射工具natapp
 * 2. 修改MaterialManage类的DOMAIN，检查代码，无误后启动服务器
 * 3. 浏览器登陆微信测试号，修改‘体验接口权限表-网页服务-网页账号的网页授权获取用户基本信息’，如：r257ku.natappfree.cc
 * 4. 在测试号首页，修改接口配置信息，填写URL和token。
 */
@Slf4j
@RestController
public class WeChatController {

    /**
     * 接入微信服务器
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/access")
    public static JsonResult access(HttpServletRequest request, HttpServletResponse response) {
        System.err.println("/access");
        try {
            AccessGuide.doGet(request, response);
            return JsonResult.ok("Access to wechat server successfully");
        } catch (Exception e) {
            e.printStackTrace();
            new AccessException("接入微信服务器异常！");
            return JsonResult.errMsg("接入微信服务器异常!");
        }
    }



    /*
        创建微信菜单
     */
    @GetMapping("/createMenu")
    public static JsonResult createMenu() {
        try {
            String menu = CustomerMenu.createMenu();
            System.err.println(menu);
            return JsonResult.ok(menu);
        } catch (Exception e) {
            new MenuCreateException("创建菜单异常！");
            return JsonResult.errMsg("创建菜单异常");
        }
    }

    /**
     * 用户点击菜单‘空分云’按钮，发送‘/login’请求,再发送‘/code’
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/code")
    public static JsonResult login(HttpServletRequest request, HttpServletResponse response) {
        try {
            String s = Authorization.CodeUrl();
            log.info("CodeUrl:" + s);
            response.sendRedirect(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JsonResult.ok("User Login Successfully");
    }

    @GetMapping("/redirect_uri")
    public static JsonResult redirect(HttpServletRequest request, HttpServletResponse response) {
        try {
//            获取code
            String code = request.getParameter("code");
//            用code换access_token
            String tokenAndOpenId = Authorization.getTokenAndOpenId(code);
//            通过access_token和openid拉取用户信息
            JSONObject jobj = JSONObject.fromObject(tokenAndOpenId);
            String openid = jobj.getString("openid");
            String userInfo = Authorization.getUserInfo(openid, jobj.getString("access_token"));
//            日志
            log.info("code:" + code);
            log.info(tokenAndOpenId);
            log.info("userInfo:" + userInfo);
            System.err.println("/isBrother?id=" + openid);
            response.sendRedirect("/isBrother?openid=" + openid);
            return JsonResult.ok("User Authorization Successfully");
        } catch (Exception e) {
            new AccessException("用户授权失败！");
            return JsonResult.errMsg("用户授权失败！");
        }
    }
}
