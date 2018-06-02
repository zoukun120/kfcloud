package com.zk.kfcloud;

import com.zk.kfcloud.Exception.MenuCreateException;
import com.zk.kfcloud.Utils.wechat.CustomerMenu;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@SpringBootApplication
@MapperScan("com.zk.kfcloud.Dao")
@EnableScheduling
public class KfcloudApplication {
    public static void main(String[] args) {
        SpringApplication.run(KfcloudApplication.class, args);
    }
}
