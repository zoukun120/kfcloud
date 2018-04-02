package com.zk.kfcloud.Controller;

import com.zk.kfcloud.Exception.AccessException;
import com.zk.kfcloud.Utils.JsonResult;
import com.zk.kfcloud.Utils.wechat.AccessGuide;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
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
}
