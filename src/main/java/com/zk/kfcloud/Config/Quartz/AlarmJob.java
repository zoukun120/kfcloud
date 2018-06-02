//package com.zk.kfcloud.Config.Quartz;
//
//import com.zk.kfcloud.Dao.FactoryMapper;
//import com.zk.kfcloud.Service.FactoryService;
//import lombok.extern.slf4j.Slf4j;
//import org.quartz.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Configurable;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.io.Serializable;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Map;
//
//
///**
// * 实现Job接口，重写execute，实现报警功能
// */
//@Slf4j
//@Component
//public class AlarmJob implements Job,Serializable {
//
//    @Autowired
//    FactoryService factoryService;//注入失败?（已搞定）
//
//    @Override
//    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException{
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        System.err.println("当前时间为："+sdf.format(new Date()));
//        Map<String, Object> kf0002_201804 = factoryService.getData("KF0002_201804","TIME,Alarm01,Alarm02");
//        System.err.println("kf0002_201804对象"+kf0002_201804);
//        System.err.println("TIME："+kf0002_201804.get("TIME"));
//        System.err.println("Alarm01："+kf0002_201804.get("Alarm01"));
//        System.err.println("Alarm02："+kf0002_201804.get("Alarm02"));
//    }
//
//}
