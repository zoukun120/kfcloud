package com.zk.kfcloud;

import com.alibaba.fastjson.JSONObject;
import com.zk.kfcloud.Exception.MenuCreateException;
import com.zk.kfcloud.Utils.wechat.CustomerMenu;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@EnableCaching //开启缓存
@EnableTransactionManagement // 开启事务管理
@SpringBootApplication
@MapperScan("com.zk.kfcloud.Dao")
public class KfcloudApplication {
    public static void main(String[] args) {
        SpringApplication.run(KfcloudApplication.class, args);
        KfcloudApplication.creatMenu();
    }

    /*
        创建微信菜单
     */
    public static void creatMenu(){
        try{
            String menu = CustomerMenu.createMenu();
            log.info(menu);
        }catch (Exception e){
            new MenuCreateException("创建菜单异常！");
            e.getStackTrace();
        }
    }

}
