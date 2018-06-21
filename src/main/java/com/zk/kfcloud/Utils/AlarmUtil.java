package com.zk.kfcloud.Utils;

import com.zk.kfcloud.Config.Quartz.ScheduledTasks;
import com.zk.kfcloud.Utils.wechat.Template;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class AlarmUtil {

    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 生产数据监控
     * @param tableName
     * @param historyStatus
     * @param currentStatus
     * @param Openids
     * @param factoryName
     * @param alarmInfo
     * @return
     * @throws ParseException
     */
    public static String alarmLogic1(String tableName, Map<String, Object> historyStatus, Map<String, Object> currentStatus,List<String> Openids,String factoryName,Map<String, Object> alarmInfo) throws ParseException {

        // 1、初始化 alarmNameList、ff、sf 数组、alarmTime
        Map<String, Object> items = initTemp1(historyStatus, currentStatus);

        List<String> alarmNameList = (List<String>)items.get("alarmNameList");
        List<Boolean> ff = (List<Boolean>)items.get("ff");
        List<Boolean> sf = (List<Boolean>)items.get("sf");
        String alarmTime = String.valueOf(items.get("alarmTime"));
//        log.info("ff大小" + ff.size() + "->" + ff);
//        log.info("sf大小" + sf.size() + "->" + sf);
        /**
         *  2、报警业务（初始化、推送逻辑）
         *  0->0 正常
         *  0->1 报警一次,如xxx表，什么字段，报警内容
         *  1->0 恢复正常
         *  1->1 此报警状态若维持5分钟以上就在此触发报警
         */

        for (int i = 0; i < ff.size(); i++) {
            Boolean one = ff.get(i);
            Boolean two = sf.get(i);
            String alarmName = alarmNameList.get(i);

            if (one && two) {//1->1，项目启动时，有些字段就处于高报警状态，则用启动时的状态初始化reference
                if (ScheduledTasks.reference1.containsKey(tableName)) {
                    if (ScheduledTasks.reference1.get(tableName).containsKey(alarmName)) {
                        String ALARMTIME = ScheduledTasks.reference1.get(tableName).get(alarmName);
                        if (ALARMTIME.contains("报警时间不详")){
                            String realALARMTIME = ALARMTIME.substring(0,ALARMTIME.indexOf("报"));
                            Long past  = dateFormat.parse(realALARMTIME).getTime();//java.text.ParseException: Unparseable date: "报警时间不详!"
                            Long now = dateFormat.parse(alarmTime).getTime();
                            // 日志输出
                            log.info("时间间隔"+(now - past)/1000+"s");
                            // 指定时间间隔 发送报警信息
                            if ((now - past)!=0 && ((now - past) % (1000 * 60 * 2) == 0)){
                                String contentName = getContentName(alarmName,alarmInfo);
                                String content = String.valueOf(alarmInfo.get(contentName));
//                                log.info(factoryName + "：报警位" + alarmName + ",报警内容content:" + content);
//                                sendAlarmMsg(factoryName, "报警时间不详!", content, Openids);
                            }
                        }
                        else {
                            Long past  = dateFormat.parse(ALARMTIME).getTime();//java.text.ParseException: Unparseable date: "报警时间不详!"
                            Long now = dateFormat.parse(alarmTime).getTime();
                            if (((now - past) % (1000 * 60 * 2) == 0)) {//(now - past)!=0
                                String contentName = getContentName(alarmName,alarmInfo);
                                String content = String.valueOf(alarmInfo.get(contentName));
//                                log.info(factoryName + "：报警位" + alarmName + ",报警内容content:" + content);
//                                sendAlarmMsg(factoryName, alarmTime, content, Openids);
                            }
                        }

                    } else {
                        Map<String, String> map = new LinkedHashMap<>();
                        map.put(alarmName, alarmTime);
                        ScheduledTasks.reference1.put(tableName, map);
                    }
                } else {
                    Map<String, String> map = new LinkedHashMap<>();
                    map.put(alarmName, alarmTime);
                    ScheduledTasks.reference1.put(tableName, map);
                }
            }
            else if (one || two) {
                if (one) {//1->0
                    ScheduledTasks.reference1.get(tableName).remove(alarmName);
                }
                else {//0->1
                    String contentName = getContentName(alarmName,alarmInfo);
                    String content = String.valueOf(alarmInfo.get(contentName));
                    log.info(factoryName+"：报警位"+alarmName+",报警内容content:"+content);
                    sendAlarmMsg(factoryName,alarmTime,content,Openids);
                    //记录xx表的xx时间xx字段报警，以便1-1的时间判断
                    Map<String,String> map = new LinkedHashMap<>();
                    map.put(alarmName,alarmTime);
                    ScheduledTasks.reference1.put(tableName,map);
                }
            }

        }
        return null;
    }
/*
**指标逻辑判断推送
 */
public static String alarmLogic2(String tableName, Map<String, Object> historyStatus, Map<String, Object> currentStatus,List<String> Openids,String factoryName,Map<String, Object> alarmInfo) throws ParseException {
    double standard1 = 100;//预警标准
    double standard2 = 110;//报警标准
    // 1、初始化 alarmNameList、ff、sf 数组、alarmTime
    Map<String, Object> items = initTemp2(historyStatus, currentStatus,standard1,standard2);

    List<String> alarmNameList = (List<String>)items.get("alarmNameList");
    List<Boolean> ff1 = (List<Boolean>)items.get("passFlag1");
    List<Boolean> sf1 = (List<Boolean>)items.get("nowFlag1");
    List<Boolean> ff2 = (List<Boolean>)items.get("passFlag2");
    List<Boolean> sf2 = (List<Boolean>)items.get("nowFlag2");
    String alarmTime = String.valueOf(items.get("alarmTime"));
    List<Double> Value = (List<Double>)items.get("value");
//        log.info("ff大小" + ff.size() + "->" + ff);
//        log.info("sf大小" + sf.size() + "->" + sf);
    /**
     *  2、报警业务（初始化、推送逻辑）
     *  0->0 正常
     *  0->1 报警一次,如xxx表，什么字段，报警内容
     *  1->0 恢复正常
     *  1->1 此报警状态若维持5分钟以上就在此触发报警
     */

    for (int i = 0; i < ff1.size(); i++) {
        Boolean one1 = ff1.get(i);//前一时刻预警
        Boolean two1 = sf1.get(i);//当前时刻预警
        Boolean one2 = ff2.get(i);//前一时刻报警
        Boolean two2 = sf2.get(i);//当前时刻报警
        String alarmName = alarmNameList.get(i);
        double value = Value.get(i);
            if((!one1)&&two1){
                String content  = "当前N2质量状态指数为"+value+",超过阈值100!" ;
                log.info(factoryName+"：报警位"+alarmName+",报警内容content:"+content);
              AlarmUtil.sendAlarmMsg( factoryName, alarmTime, content, Openids);
            }
        if((!one2)&&two2){
            String content  = "当前N2质量状态指数为"+value+",超过阈值110!" ;
            log.info(factoryName+"：报警位"+alarmName+",报警内容content:"+content);
            AlarmUtil.sendAlarmMsg( factoryName, alarmTime, content, Openids);
        }
    }
    return null;
}

    /**
     * 调用微信接口，实现推送逻辑(模板消息)
     */
    public static String sendAlarmMsg(String factoryName,String alarmTime,String content,List<String> Openids) {
        String res = null;
        for (int i = 0; i < Openids.size(); i++) {
            res = Template.send(Openids.get(i),factoryName,alarmTime,content);
        }
        return  res;
    }


    /**
     * 用报警字段，拼接报警内容字段
     * @param alarmName
     * @return
     */
    public static String getContentName(String alarmName,Map<String, Object> alarmInfo) {
//
             String Key=null;
        for (Map.Entry<String, Object> AlarmInfo : alarmInfo.entrySet()) {
            if (AlarmInfo.getValue().equals(alarmName) ) {
                String key = AlarmInfo.getKey();//取键值
                Key = key.replace("name", "content");
            }}
                return Key;

            }


            /**
             * 初始化 alarmNameList、ff、sf 数组，避免代码重复
             * @param historyStatus
             * @param currentStatus
             * @return
             */
            public static Map<String, Object> initTemp1 (Map < String,Object > historyStatus, Map < String,Object > currentStatus){
                List<String> alarmNameList = new ArrayList<>();
                List<Boolean> ff = new ArrayList<>();
                List<Boolean> sf = new ArrayList<>();
                String alarmTime = null;
                // 1 初始化 alarmNameList、ff、sf 数组
                for (Map.Entry<String, Object> historyMeta : historyStatus.entrySet()) {
                    if ("TIME".equals(historyMeta.getKey())) {//获取time
                        alarmTime = dateFormat.format(historyMeta.getValue());
                    } else {
                        alarmNameList.add(historyMeta.getKey());
                        ff.add((Boolean) historyMeta.getValue());
                    }
                }
                for (Map.Entry<String, Object> currentMeta : currentStatus.entrySet()) {
                    if (!"TIME".equals(currentMeta.getKey())) {//去掉time的后的map，全是报警位
                        sf.add((Boolean) currentMeta.getValue());
                    }
                }
                Map<String, Object> res = new LinkedHashMap<>();
                res.put("alarmNameList", alarmNameList);
                res.put("ff", ff);
                res.put("sf", sf);
                res.put("alarmTime", alarmTime);
                return res;
            }

            //初始化逻辑方便判断
    public static Map<String, Object> initTemp2 (Map < String, Object > historyStatus, Map < String, Object > currentStatus,double standard1,double standard2){
        List<String> alarmNameList = new ArrayList<>();
        List<Boolean> passFlag1 = new ArrayList<>();//指标过100预警
        List<Boolean> nowFlag1 = new ArrayList<>();
        String alarmTime = null;
        List<Double> value = new ArrayList<>();
        List<Boolean> passFlag2 = new ArrayList<>();//指标过110报警
        List<Boolean> nowFlag2 = new ArrayList<>();//
        // 1 初始化 alarmNameList、passFlag1、nowFlag1等数组
//        历史报警信息加载
        for (Map.Entry<String, Object> historyMeta : historyStatus.entrySet()) {//
            if ("TIME".equals(historyMeta.getKey())) {//获取time
                alarmTime = dateFormat.format(historyMeta.getValue());
            } else {
                alarmNameList.add(historyMeta.getKey());
                if((Double) historyMeta.getValue()>standard1){
                    passFlag1.add(true);
                } else{
                    passFlag1.add(false);
                }
                if((Double) historyMeta.getValue()>standard2) {
                    passFlag2.add(true);
                } else {
                    passFlag2.add(false);
                }
            }
        }
//        当前报警信息加载
        for (Map.Entry<String, Object> currentMeta : currentStatus.entrySet()) {
            if (!"TIME".equals(currentMeta.getKey())) {//去掉time的后的map，全是报警位
                alarmNameList.add(currentMeta.getKey());
                value.add((Double) currentMeta.getValue());
                if((Double) currentMeta.getValue()>standard1){
                    nowFlag1.add(true);
                } else{
                    nowFlag1.add(false);
                }
                if((Double) currentMeta.getValue()>standard2) {
                    nowFlag2.add(true);
                } else {
                    nowFlag2.add(false);
                }
            }
        }
        Map<String, Object> res = new LinkedHashMap<>();
        res.put("alarmNameList", alarmNameList);
        res.put("passFlag1", passFlag1);
        res.put("nowFlag1", nowFlag1);
        res.put("passFlag2", passFlag2);
        res.put("nowFlag2", nowFlag2);
        res.put("alarmTime", alarmTime);
        res.put("value", value);
        return res;
    }
            /**
             * 初始化 reference map,封装的目的是优化主代码
             *
             *  reference里
             *      *      是否有tableName？
             *      *          是
             *      *              是否有alarmName？
             *      *                  是
             *      *                      时间间隔是否是5分钟的倍数？
             *      *                          是   -------------   就推送报警消息。
             *      *                          否   -------------   什么也不做。
             *      *                  否   ---------------------   添加tableName，alarmName，alarmTime。
             *      *          否           ---------------------   添加tableName，alarmName，报警时间不详。
             *
             *
             * @param tableName
             * @param historyStatus
             * @param currentStatus
             * @return
             */
            public static void initReference1 (String tableName, Map < String, Object > historyStatus, Map < String, Object > currentStatus){
                // 1、初始化 alarmNameList、ff、sf 数组、alarmTime
                Map<String, Object> items = initTemp1(historyStatus, currentStatus);//把所有数据分开便于使用

                List<String> alarmNameList = (List<String>) items.get("alarmNameList");
                List<Boolean> ff = (List<Boolean>) items.get("ff");
                List<Boolean> sf = (List<Boolean>) items.get("sf");
                String alarmTime = String.valueOf(items.get("alarmTime"));

                // 2、初始化 reference map

                for (int i = 0; i < ff.size(); i++) {//初始化reference
                    Boolean one = ff.get(i);
                    Boolean two = sf.get(i);
                    String alarmName = alarmNameList.get(i);

                    if (one && two) {//1->1
                        if (ScheduledTasks.reference1.containsKey(tableName)) {
                            if (ScheduledTasks.reference1.get(tableName).containsKey(alarmName)) {

                            } else {
                                ScheduledTasks.reference1.get(tableName).put(alarmName, alarmTime + "报警时间不详");//初始化报警消息
                            }
                        } else {
                            Map<String, String> map = new LinkedHashMap<>();
                            map.put(alarmName, alarmTime + "报警时间不详");
                            ScheduledTasks.reference1.put(tableName, map);
                        }
                    } else if (one || two) {
                        if (one) {//1->0
                            ScheduledTasks.reference1.get(tableName).remove(alarmName);
                        } else {//0->1
                            if (ScheduledTasks.reference1.containsKey(tableName)) {
                                ScheduledTasks.reference1.get(tableName).put(alarmName, alarmTime);
                            } else {
                                Map<String, String> map = new LinkedHashMap<>();
                                map.put(alarmName, alarmTime);
                                ScheduledTasks.reference1.put(tableName, map);
                            }
                        }
                    }
                }

            }

        }
