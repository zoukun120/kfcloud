package com.zk.kfcloud.Dao;

import com.zk.kfcloud.Entity.web.WeChat;
import com.zk.kfcloud.Entity.web.WebWeChat;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface WeChatMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(WeChat record);

    int insertSelective(WeChat record);

    WeChat selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WeChat record);

    int updateByPrimaryKey(WeChat record);

    List<WeChat> findAllWeChatUser();

    void updatealarm_authTure(String openid);

    void updatealarm_authFalse (String openid);

    void updateLoginStatus(@Param("openid") String openid,@Param("loginStatus") Boolean loginStatus);

    List<WebWeChat>  findAllUser();

}