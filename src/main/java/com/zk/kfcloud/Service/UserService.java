package com.zk.kfcloud.Service;

import com.zk.kfcloud.Entity.web.User;
import com.zk.kfcloud.Exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserService {
    List<User> findAllUsers();

    User selectByPrimaryKey(int id) throws UserNotFoundException;
}
