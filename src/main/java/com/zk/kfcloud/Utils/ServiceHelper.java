package com.zk.kfcloud.Utils;


import com.zk.kfcloud.Service.MenuService;
import com.zk.kfcloud.Service.RoleService;
import com.zk.kfcloud.Service.UserService;

public final class ServiceHelper {
	public static Object getService(String serviceName) {
		return Const.WEB_APP_CONTEXT.getBean(serviceName);
	}

	public static UserService getUserService() {
		return (UserService) getService("userService");
	}

	public static RoleService getRoleService() {
		return (RoleService) getService("roleService");
	}

	public static MenuService getMenuService() {
		return (MenuService) getService("menuMappper");
	}
}
