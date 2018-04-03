package com.zk.kfcloud.ServerImpl;

import com.zk.kfcloud.Dao.UserMapper;
import com.zk.kfcloud.Dao.WeChatMapper;
import com.zk.kfcloud.Entity.web.User;
import com.zk.kfcloud.Entity.web.WeChat;
import com.zk.kfcloud.Exception.UserNotFoundException;
import com.zk.kfcloud.Service.UserService;
import com.zk.kfcloud.Utils.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service()
public class UserServiceImpl implements UserService  {

        @Autowired
        private UserMapper userMapper;

        @Autowired
        private WeChatMapper weChatMapper;

        /*
            判断当前openid是否已经在数据库，
            如果存在，返回User对象
            否则，返回null
         */
        @Override
        public User isBrother(String openid) {
            if (Tools.notEmpty(openid)){
                List<WeChat> allWeChatUser = weChatMapper.findAllWeChatUser();
                if (allWeChatUser != null){
                    for (WeChat wx:allWeChatUser) {
                        if (openid.equals(wx.getOpenId())){
                            return userMapper.selectByPrimaryKey(wx.getUserId());
                        }
                    }
                }
            }
            return null;
        }

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
