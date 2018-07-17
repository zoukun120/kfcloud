package com.zk.kfcloud.Utils.wechat;


import com.zk.kfcloud.Entity.wechat.Button;
import com.zk.kfcloud.Entity.wechat.Menu;
import com.zk.kfcloud.Entity.wechat.ViewButton;
import com.zk.kfcloud.Utils.RequestMethod;
import net.sf.json.JSONObject;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class CustomerMenu {

    public static final String MENU_CLICK = "click";
    public static final String MENU_VIEW = "view";
    public static final String MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
    public static final String WXlOGIN_URL = MaterialManage.DOMAIN + "/login";


    public static String createMenu() throws ParseException {
        System.err.println("WXlOGIN_URL:" + WXlOGIN_URL);
        String url = MENU_URL.replace("ACCESS_TOKEN", MaterialManage.getAccessToken().getAccess_token());
        System.err.println("MENU_URL:"+MENU_URL);
        return RequestMethod.doPost(url, JSONObject.fromObject(initMenu()).toString());
    }

    public static Menu initMenu() {
        ViewButton button1 = new ViewButton();
        button1.setName("空分云");
        button1.setType(MENU_VIEW);
        button1.setUrl(WXlOGIN_URL);

//        ViewButton button3 = new ViewButton();
//        button3.setName("技术支持");
//        button3.setType(MENU_VIEW);
//        button3.setUrl("https://blog.csdn.net/edison_03");


        Menu menu = new Menu();
        menu.setButton(new Button[]{button1});
        return menu;
    }

}
