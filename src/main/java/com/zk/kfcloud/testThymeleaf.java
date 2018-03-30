package com.zk.kfcloud;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testThymeleaf {

    @GetMapping("/testThymeleaf")
    public static  String test(){
        return "index";
    }
}
