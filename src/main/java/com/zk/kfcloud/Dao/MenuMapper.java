package com.zk.kfcloud.Dao;

import com.zk.kfcloud.Entity.web.Menu;

import java.util.List;

public  interface MenuMapper{
	
  public abstract List<Menu> listAllParentMenu();
  
  public abstract List<Menu> listSubMenuByParentId(Integer paramInteger);
  
  public abstract Menu getMenuById(Integer paramInteger);

  public abstract List<Integer> getMenuIdByUserId(Integer userId);

  public abstract Integer getAlarm_authByOpenId(String openid);

  public abstract List<Menu> listAllSubMenu();
  
  public abstract void insertMenu(Menu paramMenu);
  
  public abstract void updateMenu(Menu paramMenu);
  
  public abstract void deleteMenuById(Integer paramInteger);
}
