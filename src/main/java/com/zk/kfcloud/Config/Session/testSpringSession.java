package com.zk.kfcloud.Config.Session;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Spring-Session使用（https://www.jianshu.com/p/ece9ac8e2f81）
 * 1.maven依赖spring-session-data-redis
 * 2.@EnableRedisHttpSession注解（写在启动程序KfcloudApplication上）启用Spring session
 * 3.application.properties配置Redis的连接属性
 * 4.controller中测试，是否成功启用session
 */
@RestController
public class testSpringSession {
    @GetMapping("/setUrl")
    public Map<String,Object> setUrl(HttpServletRequest request){
        request.getSession().setAttribute("url", request.getRequestURL());
        Map<String,Object> map = new HashMap<>();
        map.put("url", request.getRequestURL());
        return map;
    }

    @GetMapping("/getSession")
    public Map<String,Object> getSession(HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        map.put("sessionId", request.getSession().getId());
        map.put("url", request.getSession().getAttribute("url"));
        return map;
    }
}
