package com.zk.kfcloud.Dao;

import com.zk.kfcloud.Entity.web.Role;

import java.util.List;

public interface RoleMapper {
//    int deleteByPrimaryKey(Integer roleId);
//
//    int insert(Role record);
//
//    int insertSelective(Role record);
//
//    Role selectByPrimaryKey(Integer roleId);
//
//    int updateByPrimaryKeySelective(Role record);
//
//    int updateByPrimaryKey(Role record);

    public abstract List<Role> listAllRoles();

    public abstract Role getRoleById(int paramInt);

    public abstract Integer getRoleIdByRoleName(String paramString);

    public abstract void insertRole(Role paramRole);

    public abstract void updateRoleBaseInfo(Role paramRole);

    public abstract void deleteRoleById(int paramInt);

    public abstract int getCountByName(Role paramRole);

    public abstract void updateRoleRights(Role paramRole);

    public abstract String getRoleByUserId(Integer paramInteger);
}