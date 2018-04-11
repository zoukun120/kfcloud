package com.zk.kfcloud.Controller;

import com.zk.kfcloud.Dao.WeChatMapper;
import com.zk.kfcloud.Entity.web.*;
import com.zk.kfcloud.Exception.UserNotFoundException;
import com.zk.kfcloud.Service.MenuService;
import com.zk.kfcloud.Service.RedisService;
import com.zk.kfcloud.Service.UserService;
import com.zk.kfcloud.Utils.Tools;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;


@Slf4j
@Controller
public class LoginController {

    @Autowired
    private UserService userService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private WeChatMapper weChatMapper;

    @Autowired
    private RedisService redisService;

    /**
     * 一切请求从get方式的login起，然后再接入微信api，获取到openid传到login.html
     * @return
     */
    @GetMapping("/login")
    public String login(){

        return "login";
    }

    /*
        只有新微信用户才会登陆
        登陆成功后，将userid和openid保存在tb2_wechat表中
        以实现多微信用户登陆。
     */

    @PostMapping("/login")
    public String login(String username,String password,String openid) throws UserNotFoundException {
        System.err.println("username:"+username);
        System.err.println("password:"+Tools.md5(password));
        System.err.println("openid:"+openid);
        User u = new User();
        u.setLoginname(username);
        u.setPassword(Tools.md5(password));
        Integer userid = userService.selectByNameAndPwd(u);
        System.err.println("userid:"+userid);
        User user = userService.selectByPrimaryKey(userid);
        System.err.println("当前用户："+user);
        if(userid!=0){//表示该用户登陆成功
            //记录当前用户的登陆状态status=1
            user.setStatus(1);
            userService.updateUserLoginStatus(user);
            user.setLastLogin(new Date());
            userService.updateLastLogin(user);
            log.info("用户登录状态："+user.getStatus()+",已添加sessionUser");
            //在tb2_wechat表中插入openid和userid信息
            List<WeChat> allWeChatUser = weChatMapper.findAllWeChatUser();
            Boolean flag = true;
            System.err.println(openid);
            for (WeChat wxUser:allWeChatUser) {
                System.err.println(wxUser);
                if (openid.equals(wxUser.getOpenId())) {//openid存在，不插入数据库
                    flag = false;
                }
            }
            if (flag){
                WeChat wx = new WeChat();
                wx.setUserId(userid);
                wx.setOpenId(openid);
                weChatMapper.insert(wx);
            }
            return "redirect:index";
        }else {
           return "/login";
        }
    }

    /**
     * 点击登陆按钮跳转到首页
     * @return
     */
//    @GetMapping("/index")
//    public String index(HttpSession session, Model model){
//        User user = (User) session.getAttribute("sessionUser");
//        System.err.println("user 权限："+user);
//        user = userService.getUserAndRoleById(user.getUserId());
//        Role role = user.getRole();
//        String roleRights = role != null ? role.getRights() : "";
//        String userRights = user.getRights();
//        System.err.println("用户权限："+userRights+"，角色权限："+roleRights);
//        session.setAttribute("sessionRoleRights", roleRights);
//        session.setAttribute("sessionUserRights", userRights);
//        List<Menu> menuList = menuService.listAllMenu();
//        if ((Tools.notEmpty(userRights)) || (Tools.notEmpty(roleRights))) {
//            for (Menu menu : menuList) {
//                menu.setHasMenu((RightsHelper.testRights(userRights, menu.getMenuId().intValue()))
//                        || (RightsHelper.testRights(roleRights, menu.getMenuId().intValue())));
//                if (menu.isHasMenu()) {
//                    List<Menu> subMenuList = menu.getSubMenu();
//                    for (Menu sub : subMenuList) {
//                        sub.setHasMenu((RightsHelper.testRights(userRights, sub.getMenuId().intValue()))
//                                || (RightsHelper.testRights(roleRights, sub.getMenuId().intValue())));
//                    }
//                }
//            }
//        }
////		System.err.println("goto index.jsp，get sessionUser");
//		for (Menu menu : menuList) {
//			log.info("pc菜单列表：" + menu.getMenuId() + "," + menu.getMenuName());
//		}
//        JSONArray menuLists = JSONArray.fromObject(menuList);
////		System.out.println(menuLists);
//        model.addAttribute("user", user);
//        model.addAttribute("menuLists", menuLists);
//        return "index";
//    }
}
