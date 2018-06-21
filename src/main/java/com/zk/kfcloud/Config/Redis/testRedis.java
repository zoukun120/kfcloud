//package com.zk.kfcloud.Config.Redis;
//
//import com.zk.kfcloud.Entity.web.User;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * 如何使用Redis？（http://blog.didispace.com/springbootredis/）
// * 1.application.properties配置Redis的连接属性和maven依赖spring-boot-starter-redis
// * 2.简单存取string类型的数据，直接@Autowired：private StringRedisTemplate stringRedisTemplate
// * 3.高级存取obj类型的数据
// *      3.1 实体对象（User）实现Serializable 接口
// *      3.2 序列化对象（RedisObjectSerializer ）实现RedisSerializer<Object>接口
// *      3.3 RedisConfig中配置Redis对象模板（RedisTemplate<String, User>）
// */
//@RestController
//public class testRedis {
//
//    @Autowired
//    StringRedisTemplate stringRedisTemplate;
//
//    @GetMapping("/string")
//    public String testString(){
//        stringRedisTemplate.opsForValue().set("string","string redis");
//        return stringRedisTemplate.opsForValue().get("string");
//    }
//
//
//    @Autowired
//    RedisTemplate<String,User> userRedisTemplate;
//
//    @GetMapping("/object")
//    public User testObject(){
//        User user = new User();
//        user.setLoginname("Ed Sheeran");
//        user.setPassword("123456");
//        userRedisTemplate.opsForValue().set("user",user);
//        return userRedisTemplate.opsForValue().get("user");
//    }
//}
