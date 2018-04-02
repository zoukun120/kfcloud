package com.zk.kfcloud.ServerImpl;

import com.zk.kfcloud.Dao.UserMapper;
import com.zk.kfcloud.Entity.web.User;
import com.zk.kfcloud.Exception.UserNotFoundException;
import com.zk.kfcloud.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service()
public class UserServiceImpl implements UserService  {

        @Autowired
        private UserMapper userMapper;

        @Override
        public List<User> findAllUsers() {
             List<User> allUsers = userMapper.findAllUsers();
             return allUsers;
        }

        @Override
        public User selectByPrimaryKey(int id) throws UserNotFoundException {
            User user = userMapper.selectByPrimaryKey(id);
                if (user == null) {
                    throw new UserNotFoundException(10001,"用户名不存在");
                }
                return user;
        }
}
