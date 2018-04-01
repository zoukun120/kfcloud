package com.zk.kfcloud.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testDevTools {

    @GetMapping("/testDevTools")
    public static  String test(){
        return "aaaa";
    }
}







