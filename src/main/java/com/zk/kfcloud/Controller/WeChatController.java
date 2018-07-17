package com.zk.kfcloud.Controller;

import com.zk.kfcloud.Exception.AccessException;
import com.zk.kfcloud.Exception.MenuCreateException;
import com.zk.kfcloud.Utils.JsonResult;
import com.zk.kfcloud.Utils.wechat.*;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

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
        try {
            AccessGuide.doGet(request, response);
            return JsonResult.ok("Access to wechat server successfully");
        } catch (Exception e) {
            e.printStackTrace();
            new AccessException("接入微信服务器异常！");
            return JsonResult.errMsg("接入微信服务器异常!");
        }
    }

    /**
     * 用户给公众号发送的消息，微信服务器会以xml的形式post到自己配置服务器的url上
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @PostMapping("/access")
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String responseMessage = MsgManage.processRequest(req);//工具类 coreService的processRequest方法处理用户请求
        PrintWriter out = resp.getWriter();
        out.print(responseMessage);
        out.close();
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
            String access_token = jobj.getString("access_token");
            //从用户信息中提取用户名称
//            String userInfo = Authorization.getUserInfo( access_token,openid);
//            JSONObject userinfo = JSONObject.fromObject(userInfo);
//            String nickname= userinfo.getString("nickname");
//            //获取该公众号的所有open_id
//            String OpenIDInfo = Authorization.OpenID_URL(MaterialManage.getAccessToken().getAccess_token());
//            JSONObject OpenIDinfo = JSONObject.fromObject(OpenIDInfo);
//            JSONObject OpenID= OpenIDinfo.getJSONObject("data");
//            System.err.println("OpenID;"+OpenID);


            // 日志
            log.info("code:" + code);
            log.info(tokenAndOpenId);
//            log.info("userInfo:" + userInfo);
            log.info("/isBrother?openid="+openid+"&access_token="+ access_token);
            response.sendRedirect("/isBrother?openid=" + openid + "&access_token=" + access_token);
            return JsonResult.ok("User Authorization Successfully");
        } catch (Exception e) {
            new AccessException("用户授权失败！");
            return JsonResult.errMsg("用户授权失败！");
        }
    }
}
