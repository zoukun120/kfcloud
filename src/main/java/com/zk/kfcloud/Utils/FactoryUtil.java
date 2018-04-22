package com.zk.kfcloud.Utils;

import com.zk.kfcloud.Entity.web.Menu;
import com.zk.kfcloud.Service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FactoryUtil {

	public static String assemblyModelField(int paraNum, String initField) {
		StringBuilder sb = new StringBuilder(initField);
		for (int i = 1; i <= paraNum; i++) {
			String paraName = "para" + i + "_name";
			String paraSuffix = "para" + i + "_suffix";
			sb.append(paraName + ",");
			sb.append(paraSuffix + ",");
		}
		String fields = sb.substring(0, sb.toString().lastIndexOf(","));
		return fields;
	}

	public static String assemblyKfField(Map<String, Object> paraMap, String initField) {
		StringBuilder KFFields = new StringBuilder(initField);
		for (Map.Entry<String, Object> str : paraMap.entrySet()) {
			String key = (String) str.getKey();
			if (key.contains("name")) {
				String value = (String) str.getValue();
				KFFields.append(value + ",");
			}
		}
		String SqlFields = KFFields.substring(0, KFFields.toString().lastIndexOf(","));
		System.out.println(SqlFields);
		return SqlFields;
	}

//	public  List<Menu> commonCode(Integer userid) {
//		List<Menu> menus = new ArrayList<>();
//		List<Integer> menuIds = menuService.getMenuIdByUserId(userid);
//		for (Integer menuId:menuIds) {
//			Menu menu = menuService.getMenuById(menuId);
//			if (Tools.isEmpty(menu.getMenuUrl())){
//				menu.setSubMenu(menuService.listSubMenuByParentId(menuId));
//			}
//			menus.add(menu);
//		}
//		for (Menu menu:menus) {
//			System.err.println(menu);
//		}
//		return menus;
//	}
}
