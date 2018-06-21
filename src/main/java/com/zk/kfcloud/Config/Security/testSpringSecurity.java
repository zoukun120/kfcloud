package com.zk.kfcloud.Config.Security;


import com.zk.kfcloud.Entity.web.User;
import com.zk.kfcloud.Exception.UserNotFoundException;
import com.zk.kfcloud.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 一：Spring-Security 自定义的用户认证机制及处理过程？（https://www.hellojava.com/article/815，https://www.tianmaying.com/tutorial/spring-security）
 * 自定义用户的校验机制的话，只需要实现AuthenticationProvider，即实现UserDetailsService接口（可以根据用户名获取用户信息）
 *
 * 二：如何使用Spring-Security ？（https://www.jianshu.com/p/08cc28921fd0）
 * 1. 实体对象实现2个接口（User implements Serializable, UserDetails）。
 * 2. 让UserService接口的UserServiceImpl再实现UserDetailsService接口，重载loadUserByUsername方法，目的使用自定User类代替Spring-Security的默认User类
 * 3. 编写WebSecurityConfig类，继承WebSecurityConfigurerAdapter。
 * 4. 使用@PreAuthorize注解，限制角色权限。
 */
@Controller
public class testSpringSecurity {

    @Autowired
    private UserService userService;

    /**
     * 根路径跳转到登陆页面
     * @return
     */
//    @GetMapping("/")
//    public String loginget(){
//        return "login";
//    }


//
//    /**
//     * 注销，跳到登陆页
//     * @return
//     */
////    @GetMapping("/login")
////    public String index(){
////        return "login";
////    }
//
//    @GetMapping("/user")
//    @ResponseBody
//    public User findSomeUser(){
//        User user = null;
//        try {
//            user = userService.selectByPrimaryKey(1);
//        } catch (UserNotFoundException e) {
//            e.printStackTrace();
//        }
//        return user;
//    }
}
