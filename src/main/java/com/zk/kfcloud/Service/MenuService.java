package com.zk.kfcloud.Service;

import com.zk.kfcloud.Entity.web.Menu;

import java.util.List;

public abstract interface MenuService {
    public abstract List<Menu> listAllMenu();

    public abstract List<Menu> listAllParentMenu();

    public abstract List<Menu> listSubMenuByParentId(Integer paramInteger);

    public abstract List<Menu> listAllSubMenu();

    public abstract Menu getMenuById(Integer paramInteger);

    public abstract void saveMenu(Menu paramMenu);

    public abstract void deleteMenuById(Integer paramInteger);
}
