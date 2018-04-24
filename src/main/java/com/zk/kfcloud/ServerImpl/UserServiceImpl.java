package com.zk.kfcloud.ServerImpl;

import com.zk.kfcloud.Dao.UserMapper;
import com.zk.kfcloud.Dao.WeChatMapper;
import com.zk.kfcloud.Entity.web.User;
import com.zk.kfcloud.Entity.web.WeChat;
import com.zk.kfcloud.Exception.UserNotFoundException;
import com.zk.kfcloud.Service.UserService;
import com.zk.kfcloud.Service.WeChatService;
import com.zk.kfcloud.Utils.Tools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class UserServiceImpl implements UserService,UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WeChatService weChatService;

    /*
        判断当前openid是否已经在数据库，
        如果存在，返回User对象
        否则，返回null
     */
    @Override
    public User isBrother(String openid) {
        if (Tools.notEmpty(openid)) {
            List<WeChat> allWeChatUser = weChatService.findAllWeChatUser();
            if (allWeChatUser != null) {
                for (WeChat wx : allWeChatUser) {
                    System.err.println(wx);
                    if (openid.equals(wx.getOpenId())) {
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
            throw new UserNotFoundException(10001, "用户名不存在");
        }
        return user;
    }

    /**
     * 用户存在就返回userid，不存在返回0
     *
     * @param user
     * @return
     */
    @Override
    public Integer selectByNameAndPwd(User user) {
        User u = userMapper.selectByNameAndPwd(user);
        if (u != null) {
            return u.getUserId();
        } else {
            return 0;
        }
    }

    public User getUserById(Integer userId) {
        return this.userMapper.getUserById(userId);
    }

    public boolean insertUser(User user) {
        int count = this.userMapper.getCountByName(user.getLoginname());
        if (count > 0) {
            return false;
        }
        user.setPassword(Tools.md5(user.getPassword()));
        this.userMapper.insertUser(user);
        return true;
    }

    public List<User> listPageUser(User user) {
        return this.userMapper.listPageUser(user);
    }

    public void updateUser(User user) {
        this.userMapper.updateUser(user);
    }

    public void updateUserBaseInfo(User user) {
        user.setPassword(Tools.md5(user.getPassword()));
        this.userMapper.updateUserBaseInfo(user);
    }

    public void updateUserRights(User user) {
        this.userMapper.updateUserRights(user);
    }

    public void updateUserLoginStatus(User user) {
        this.userMapper.updateUserLoginStatus(user);
    }

    public User getUserByNameAndPwd(Map<String, String> map) {
        User userInfo = this.userMapper.getUserByNameAndPwd(map);

        return userInfo;
    }

    public void deleteUser(int userId) {
        this.userMapper.deleteUser(userId);
    }

    public User getUserAndRoleById(Integer userId) {
        return this.userMapper.getUserAndRoleById(userId);
    }

    public void updateLastLogin(User user) {
        this.userMapper.updateLastLogin(user);
    }

    public List<User> listAllUser() {
        return this.userMapper.listAllUser();
    }

    public User getUserByOpenId(String openId) {
        return this.userMapper.getUserByOpenId(openId);
    }

    public List<String> listAllOpenId() {
        return this.userMapper.listAllOpenId();
    }

    /**
     * 重载UserDetailsService的loadUserByUsername，为security服务。
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.selectByName(new User(username));
        if (user == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        log.info("username:"+user.getUsername()+";password:"+user.getPassword());
        return user;
    }
}
