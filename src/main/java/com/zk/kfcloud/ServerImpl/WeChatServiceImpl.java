package com.zk.kfcloud.ServerImpl;

import com.zk.kfcloud.Dao.WeChatMapper;
import com.zk.kfcloud.Entity.web.WeChat;
import com.zk.kfcloud.Entity.web.WebWeChat;
import com.zk.kfcloud.Service.WeChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class WeChatServiceImpl implements WeChatService {

    @Autowired
    private WeChatMapper weChatMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return weChatMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(WeChat record) {
        return weChatMapper.insert(record);
    }

    @Override
    public int insertSelective(WeChat record) {
        return weChatMapper.insertSelective(record);
    }

    @Override
    public WeChat selectByPrimaryKey(Integer id) {
        return weChatMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(WeChat record) {
        return weChatMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(WeChat record) {
        return weChatMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<WeChat> findAllWeChatUser() {
        return weChatMapper.findAllWeChatUser();
    }

    @Override
    public void updateLoginStatus(String openid, Boolean loginStatus) {
        openid = "\'"+openid+"\'";
        weChatMapper.updateLoginStatus(openid,loginStatus);
    }

    @Override
    public void updateLoginTime(WeChat weChat) {
        weChatMapper.updateLoginTime(weChat);
    }

    @Override
    public void updateWecharname(WeChat weChat) {
        weChatMapper.updateWecharname(weChat);
    }

    @Override
    public void updateByopenId(Boolean state, String openid) {
       if(state==true) {
           weChatMapper.updatealarm_authTure(openid);
       }
       else{
            weChatMapper.updatealarm_authFalse(openid);
       }
    }


    @Override
    public List<WebWeChat>  findAllUser() {
        return weChatMapper.findAllUser();
    }
}
