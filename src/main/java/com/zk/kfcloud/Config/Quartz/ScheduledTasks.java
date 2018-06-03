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
    public static Map<String,Map<String,String>> reference = new LinkedHashMap<>();

    @Autowired
    FactoryService factoryService;

    @Autowired
    MenuService menuService;

    /**
     *难点：
     * （1）每一次
     * @throws ParseException
     */
    @Scheduled(fixedRate = 20000)
    public void reportAlarmStatus() throws ParseException {

        // 1、存放要监控的的表
        List<String> alarmTableList = new ArrayList<>();
//        alarmTableList.add("KF0002");
        alarmTableList.add("KF0002_201805");
        alarmTableList.add("KF0004");

        // 2、初始化historyStatusList（存放系统启动时对应数据表的最新数据）
        for (int i=0; i<alarmTableList.size();i++){
            if (historyStatusList.size() == i){
                historyStatusList.add(i,factoryService.monitor(alarmTableList.get(i)));
            }
        }
        // 3、分别查询出当前数据表的最新数据，判断是否需要报警
        List<String> factoryNames = new ArrayList<>();
        for (int i = 0; i < alarmTableList.size(); i++) {
            String tabeleName = alarmTableList.get(i);
            tabeleName = tabeleName.substring(0,6);
            List<String> factoryNames1 = factoryService.getFactoryNames(tabeleName);
            for (int j = 0; j < factoryNames1.size(); j++) {
                factoryNames.add(factoryNames1.get(j));
            }
        }
        log.info("factoryNames:"+factoryNames);


        for (int i=0; i<alarmTableList.size();i++){
            Map<String, Object> historyStatus = historyStatusList.get(i);
            Map<String, Object> currentStatus = factoryService.monitor(alarmTableList.get(i));
            String tableName = alarmTableList.get(i);
//            tableName = tabeleName.substring(0,6);

            AlarmUtil.initReference(tableName, historyStatus, currentStatus);
            System.err.println("reference初始化后："+reference);

            Map<String, Object> alarmInfo = factoryService.getAlarmInfoByAlarmUrl(tableName.substring(0,6));
            List<String> realOpenIds = factoryService.getOpenids(tableName);
//            for (int j = 0; j < realOpenIds.size(); j++) {
//               log.info(factoryNames.get(i)+"的微信用户->"+realOpenIds.get(j));
//            }
            List<String> openIds = new ArrayList<>();
            openIds.add("osAgr1Cp2vruwNuE9Z-SRrfe9LQY");
            openIds.add("osAgr1Eoe3jZu74qEve0b1_d6e7Y");

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
}
