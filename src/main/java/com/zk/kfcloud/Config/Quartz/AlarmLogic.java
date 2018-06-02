//package com.zk.kfcloud.Config.Quartz;
//
//import com.zk.kfcloud.Service.FactoryService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.Map;
//
//@Component
//public class AlarmLogic {
//
//    @Autowired
//    FactoryService factoryService;
//
//    public void monitor(){
//        long start = System.currentTimeMillis();
//        String tableName = "KF0002_201804";
//        Map<String, Object> alarmData = factoryService.getAlarmData(tableName);
//        String feilds = "TIME,";
//        for (Map.Entry<String,Object> alarm:alarmData.entrySet()) {
//            String key = alarm.getKey();
//            if(key.contains("Alarm")){
//                feilds += key+",";
//            }
//
//        }
//        feilds = feilds.substring(0,feilds.lastIndexOf(","));
//        System.err.println("feilds->"+feilds+",耗时-》"+(System.currentTimeMillis()-start));
//    }
//}
