package com.zk.kfcloud.Controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zk.kfcloud.Entity.web.Role;
import com.zk.kfcloud.Entity.web.WeChat;
import com.zk.kfcloud.Service.RoleService;
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
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/roles")
    @ResponseBody
    public Map<String, Object> pageUser(Integer page, Integer limit) {
        Page<Role> roles = PageHelper.startPage(page, limit).doSelectPage(
                () -> {
                    roleService.listAllRoles();
                }
        );
        for (Role role : roles) {
            log.info("roleId = " + role.getRoleId() + ",roleName =" + role.getRoleName());
        }
        Map<String, Object> res = new HashMap<>();
        res.put("code", 0);
        res.put("msg", "");
        res.put("count",roleService.listAllRoles().size());
        res.put("data", JSONArray.fromObject(roles));
        return res;
    }
}
