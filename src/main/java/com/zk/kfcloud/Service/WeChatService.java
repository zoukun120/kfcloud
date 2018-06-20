package com.zk.kfcloud.Service;

import com.zk.kfcloud.Entity.web.WeChat;

import java.util.List;

public interface WeChatService {

    int deleteByPrimaryKey(Integer id);

    int insert(WeChat record);

    int insertSelective(WeChat record);

    WeChat selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WeChat record);

    int updateByPrimaryKey(WeChat record);

    void updateByopenId(Boolean state, String openid);

    List<WeChat> findAllWeChatUser();
}
