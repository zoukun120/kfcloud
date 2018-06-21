package com.zk.kfcloud.ServerImpl;


import com.zk.kfcloud.Dao.MenuMapper;
import com.zk.kfcloud.Entity.web.Menu;
import com.zk.kfcloud.Service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {

	@Autowired
	private MenuMapper menuMapper;

	public void deleteMenuById(Integer menuId) {
		this.menuMapper.deleteMenuById(menuId);
	}

	public Menu getMenuById(Integer menuId) {
		return this.menuMapper.getMenuById(menuId);
	}

	@Override
	public List<Integer> getMenuIdByUserId(Integer userId) {
		return menuMapper.getMenuIdByUserId(userId);
	}


	public Integer getAlarm_authByOpenId(String openid) {
		return menuMapper.getAlarm_authByOpenId(openid);
	}


	public List<Menu> listAllParentMenu() {
		return this.menuMapper.listAllParentMenu();
	}

	public void saveMenu(Menu menu) {
		if ((menu.getMenuId() != null) && (menu.getMenuId().intValue() > 0)) {
			this.menuMapper.updateMenu(menu);
		} else {
			this.menuMapper.insertMenu(menu);
		}
	}

	public List<Menu> listSubMenuByParentId(Integer parentId) {
		return this.menuMapper.listSubMenuByParentId(parentId);
	}

	public List<Menu> listAllMenu() {
		List<Menu> rl = listAllParentMenu();
		for (Menu menu : rl) {
			List<Menu> fistSubList = listSubMenuByParentId(menu.getMenuId());
			for (Menu menu1 : fistSubList) {
				if ("".equals(menu1.getMenuUrl())) {
					List<Menu> secondSubList = listSubMenuByParentId(menu1.getMenuId());
					menu1.setSubMenu(secondSubList);
				}
			}
			menu.setSubMenu(fistSubList);
		}
		return rl;
	}

	public List<Menu> listAllSubMenu() {
		return this.menuMapper.listAllSubMenu();
	}
}
