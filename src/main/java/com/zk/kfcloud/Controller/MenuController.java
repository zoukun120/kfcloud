package com.zk.kfcloud.Controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zk.kfcloud.Entity.web.WeChat;
import com.zk.kfcloud.Service.MenuService;
import com.zk.kfcloud.Service.WeChatService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
public class MenuController {


    @Autowired
    private WeChatService weChatService;

    @GetMapping("/menus")
    @ResponseBody
    public Map<String, Object> pageUser(Integer page, Integer limit) {
        Page<WeChat> menus = PageHelper.startPage(page, limit).doSelectPage(
                () -> {
                    weChatService.findAllWeChatUser();
                }
        );
        for (WeChat menu : menus) {
            log.info("userId = " + menu.getUserId() + ",openId =" + menu.getOpenId());
        }
        Map<String, Object> res = new HashMap<>();
        res.put("code", 0);
        res.put("msg", "");
        res.put("count",weChatService.findAllWeChatUser().size());
        res.put("data", JSONArray.fromObject(menus));
        return res;
    }

}
