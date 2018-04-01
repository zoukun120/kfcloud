package com.zk.kfcloud.Controller;


import com.zk.kfcloud.Dao.UserMapper;
import com.zk.kfcloud.Entity.web.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class testMyBatis {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/findAll")
    List<User> findall(){
        List<User> allUsers = userMapper.findAllUsers();
        System.out.println(allUsers);
        return allUsers;
    }
}
