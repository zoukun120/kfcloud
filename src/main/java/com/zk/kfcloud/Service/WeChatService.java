package com.zk.kfcloud.Service;

import com.zk.kfcloud.Entity.web.WeChat;
import com.zk.kfcloud.Entity.web.WebWeChat;

import java.util.List;
import java.util.Map;

public interface WeChatService {

    int deleteByPrimaryKey(Integer id);

    int insert(WeChat record);

    int insertSelective(WeChat record);

    WeChat selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WeChat record);

    int updateByPrimaryKey(WeChat record);

    void updateByopenId(Boolean state, String openid);

    void updateWecharname(WeChat weChat);

    List<WeChat> findAllWeChatUser();

    void updateLoginStatus(String openid,Boolean loginStatus);

    List<WebWeChat> findAllUser();

    void updateLoginTime(WeChat weChat);
}
