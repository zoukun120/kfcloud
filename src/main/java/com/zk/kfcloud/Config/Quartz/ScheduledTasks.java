package com.zk.kfcloud.Config.Quartz;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.zk.kfcloud.Service.FactoryService;
import com.zk.kfcloud.Service.MenuService;
import com.zk.kfcloud.Utils.AlarmUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ScheduledTasks {

    public static  List<Map<String, Object>> historyStatusList = new ArrayList<>();
    public static  List<String> alarmTableList = new ArrayList<>();
    public static Map<String,Map<String,String>> reference = new LinkedHashMap<>();

    @Autowired
    FactoryService factoryService;

    @Autowired
    MenuService menuService;

    // 1、存放要监控的的表
    static {
        alarmTableList.add("KF0002");
//        alarmTableList.add("KF0002_201805");
        alarmTableList.add("KF0004");
    }

    /**
     * 生产数据监控
     * @throws ParseException
     */
    @Scheduled(fixedRate = 1000*20)
    public void reportAlarmStatus() throws ParseException {

        // 2、初始化historyStatusList（存放系统启动时对应数据表的最新数据）
        for (int i=0; i<alarmTableList.size();i++){
            if (historyStatusList.size() == i){
                historyStatusList.add(i,factoryService.monitor(alarmTableList.get(i)));
            }
        }
        // 3、分别查询出当前数据表的最新数据，判断是否需要报警
        List<String> factoryNames = factoryService.returnfactoryNames(alarmTableList);
        log.info("factoryNames:"+factoryNames);
        for (int i=0; i<alarmTableList.size();i++){
            Map<String, Object> historyStatus = historyStatusList.get(i);
            Map<String, Object> currentStatus = factoryService.monitor(alarmTableList.get(i));
            String tableName = alarmTableList.get(i);
            // 初始化 alarmNameList、ff、sf 数组、alarmTime、reference
            AlarmUtil.initReference(tableName, historyStatus, currentStatus);
            log.info("报警reference里："+reference);
            // 获取关注该厂的所有微信用户
            Map<String, Object> alarmInfo = factoryService.getAlarmInfoByAlarmUrl(tableName.substring(0,6));
            List<String> realOpenIds = factoryService.getOpenids(tableName);
            // 为了调试，强制指定openIds
            List<String> openIds = new ArrayList<>();
            openIds.add("osAgr1Cp2vruwNuE9Z-SRrfe9LQY");
//            openIds.add("osAgr1Eoe3jZu74qEve0b1_d6e7Y");
//            openIds.add("osAgr1Dl49z6VERy-AkEzez6ZCqQ");
            // 报警逻辑部分
            String factoryName = factoryNames.get(i);
            AlarmUtil.alarmLogic(tableName,historyStatus,currentStatus,openIds,factoryName,alarmInfo);
        }
        // 4、清空一下historyStatusList，并将currentStatus复制给historyStatus（historyStatusList.get(i)）
        historyStatusList.clear();
        for (int i=0; i<alarmTableList.size();i++){
            Map<String, Object> currentStatus = factoryService.monitor(alarmTableList.get(i));
            historyStatusList.add(currentStatus);
        }
    }


    /**
     * 分析数据监控
     * @throws ParseException
     */
    @Scheduled(fixedRate = 1000*30)
    public void reportAlarmStatus2() throws ParseException {
        // 1、查出数据表对应的工厂名称
        List<String> factoryNames = factoryService.returnfactoryNames(alarmTableList);
        // 2、执行监控逻辑
        for (int i=0; i<alarmTableList.size();i++){
            // 2.1、初始化数据
            String alarmTime = null;
            String tableName = alarmTableList.get(i);
            String factoryName = factoryNames.get(i);
            Map<String, Object> anlAlarmData = factoryService.anlAlarmLogic(tableName);
            log.info(factoryName+"最新分析数据:"+anlAlarmData);
//            List<String> realOpenIds = factoryService.getOpenids(tableName);
            // 为了调试，强制指定openIds
            List<String> openIds = new ArrayList<>();
            openIds.add("osAgr1Cp2vruwNuE9Z-SRrfe9LQY");
//            openIds.add("osAgr1Eoe3jZu74qEve0b1_d6e7Y");
//            openIds.add("osAgr1Dl49z6VERy-AkEzez6ZCqQ");
            // 2.2 主逻辑部分
            for (Map.Entry<String,Object> meta : anlAlarmData.entrySet()) {
                String key = meta.getKey();
                if (!key.contains("TIME")){
                    Float value = Float.valueOf(String.valueOf(meta.getValue()));
                    if (value>110){
                        String content  = "当前N2质量状态指数为"+value+",超过阈值110!" ;
                        AlarmUtil.sendAlarmMsg( factoryName, alarmTime, content, openIds);
                    }
                    else if (value>100){
                        String content  = "当前N2质量状态指数为"+value+",超过阈值100!" ;
                        AlarmUtil.sendAlarmMsg( factoryName, alarmTime, content, openIds);
                    }
                }
                else {
                    alarmTime = AlarmUtil.dateFormat.format(meta.getValue());
                }
            }
        }
    }

}
