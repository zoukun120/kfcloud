package com.zk.kfcloud;

import com.zk.kfcloud.Service.FactoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class KfcloudApplicationTests {

    @Autowired
    FactoryService factoryService;

    @Test
    public void contextLoads(){

    }

}
