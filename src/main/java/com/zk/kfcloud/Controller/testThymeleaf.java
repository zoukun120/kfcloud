package com.zk.kfcloud.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class testThymeleaf {

    @GetMapping("/testThymeleaf")
    public static String test() {

        return "index";

    }
}
