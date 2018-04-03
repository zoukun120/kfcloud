package com.zk.kfcloud.Controller;

import com.zk.kfcloud.Service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.UUID;

@RestController
public class testRedis {

    @Autowired
    private RedisService redisService;

    @GetMapping("/testRedis")
    public  String uid(HttpSession session) {
        UUID uid = (UUID) session.getAttribute("uid");
        if (uid == null) {
            uid = UUID.randomUUID();
        }
        redisService.set("sessionid",String.valueOf(uid));
        redisService.set("word", "正在使用Redis高级功能。。。");
        return session.getId();
    }
}
