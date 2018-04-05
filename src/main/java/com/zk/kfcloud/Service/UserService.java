package com.zk.kfcloud.Service;

import com.zk.kfcloud.Entity.web.User;
import com.zk.kfcloud.Exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserService {
    /**
     * 如果是自己人就返回该用户信息，否则返回登陆页面
     * @param openid
     * @return
     */
    User isBrother(String openid);

    List<User> findAllUsers();

    User selectByPrimaryKey(int id) throws UserNotFoundException;

    Integer selectByNameAndPwd(User user);
}
