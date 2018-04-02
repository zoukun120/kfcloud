package com.zk.kfcloud.Controller;


import com.zk.kfcloud.Entity.web.User;
import com.zk.kfcloud.Exception.UserNotFoundException;
import com.zk.kfcloud.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testSpringSecurity {

    @Autowired
    private UserService userService;

    @GetMapping("/user/{id}")
    public User findSomeUser(@PathVariable("id")int id){
        User user = null;
        try {
            user = userService.selectByPrimaryKey(id);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }
        return user;
    }
}
