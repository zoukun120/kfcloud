package com.zk.kfcloud.ServerImpl;

import com.zk.kfcloud.Dao.WeChatMapper;
import com.zk.kfcloud.Entity.web.WeChat;
import com.zk.kfcloud.Service.WeChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
