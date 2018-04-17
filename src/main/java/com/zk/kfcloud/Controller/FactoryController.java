package com.zk.kfcloud.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@Controller
public class FactoryController {

    @GetMapping("/subMenu/{pagename}/{factoryId}")
    public String sendPage(@PathVariable("pagename") String pagename, @PathVariable("factoryId") String factoryId, Model model){
        model.addAttribute("factoryId",factoryId);
        //        去掉后缀'.html'
        return pagename.substring(0, pagename.indexOf("."));
    }
}
