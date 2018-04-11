package com.zk.kfcloud.Dao;

import com.zk.kfcloud.Entity.web.User;

import java.util.List;
import java.util.Map;

public interface UserMapper {

    int deleteByPrimaryKey(Integer userId);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer userId);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    List<User> findAllUsers();

    User selectByNameAndPwd(User user);

    User selectByName(User user);

    public abstract List<User> listAllUser();

    public abstract User getUserById(Integer paramInteger);

    public abstract void insertUser(User paramUser);

    public abstract void updateUser(User paramUser);

    public abstract User getUserByNameAndPwd(Map<String, String> paramMap);

    public abstract void updateUserBaseInfo(User paramUser);

    public abstract void updateUserRights(User paramUser);

    public abstract void updateUserLoginStatus(User user);

    public abstract int getCountByName(String paramString);

    public abstract void deleteUser(int paramInt);

    public abstract int getCount(User paramUser);

    public abstract List<User> listPageUser(User paramUser);

    public abstract User getUserAndRoleById(Integer paramInteger);

    public abstract void updateLastLogin(User paramUser);

    public abstract User getUserByOpenId(String paramString);

    public abstract List<String> listAllOpenId();
}