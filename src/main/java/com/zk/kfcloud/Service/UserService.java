package com.zk.kfcloud.Service;

import com.zk.kfcloud.Entity.web.User;
import com.zk.kfcloud.Exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

public interface UserService {
    /**
     * 如果是自己人就返回该用户信息，否则返回登陆页面
     *
     * @param openid
     * @return
     */
    User isBrother(String openid);

    List<User> findAllUsers();

    User selectByPrimaryKey(int id) throws UserNotFoundException;

    Integer selectByNameAndPwd(User user);

    void updateLastLogin(User paramUser);

    public abstract User getUserById(Integer paramInteger);

    public abstract boolean insertUser(User paramUser);

    public abstract void updateUser(User paramUser);

    public abstract User getUserByNameAndPwd(Map<String, String> paramMap);

    public abstract void updateUserBaseInfo(User paramUser);

    public abstract void updateUserRights(User paramUser);

    public abstract void updateUserLoginStatus(User paramUser);

    public abstract void deleteUser(int paramInt);

    public abstract List<User> listPageUser(User paramUser);

    public abstract User getUserAndRoleById(Integer paramInteger);

    public abstract List<User> listAllUser();

    public abstract User getUserByOpenId(String paramString);

    public abstract List<String> listAllOpenId();
}
