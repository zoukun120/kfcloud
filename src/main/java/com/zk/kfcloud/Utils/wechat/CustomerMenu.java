package com.zk.kfcloud.Utils.wechat;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zk.kfcloud.Entity.wechat.Button;
import com.zk.kfcloud.Entity.wechat.Menu;
import com.zk.kfcloud.Entity.wechat.ViewButton;
import com.zk.kfcloud.Utils.RequestMethod;

import java.util.HashMap;
import java.util.Map;

public class CustomerMenu {

	public static final String MENU_CLICK ="click";
	public static final String MENU_VIEW ="view";
	public static final String MENU_URL ="https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	public static final String WXlOGIN_URL = MaterialManage.DOMAIN + "/code";


	public static String createMenu() {
		String url = MENU_URL.replace("ACCESS_TOKEN",MaterialManage.getAccessToken().getAccess_token());
		return  RequestMethod.doPost(url, JSON.toJSONString(initMenu()));
	}

	public static Menu initMenu() {
		ViewButton button1 = new ViewButton();
		button1.setName("空分云");
		button1.setType(MENU_VIEW);
		button1.setUrl(WXlOGIN_URL);

		ViewButton button3 = new ViewButton();
		button3.setName("技术支持");
		button3.setType(MENU_VIEW);
		button3.setUrl("https://blog.csdn.net/edison_03/article/category/7502343");


		Menu menu = new Menu();
		menu.setButton(new Button[] { button1,button3});
		return menu;
	}

}
