package com.zk.kfcloud.Utils;

import com.zk.kfcloud.Config.Quartz.ScheduledTasks;
import com.zk.kfcloud.Utils.wechat.Template;
import lombok.extern.slf4j.Slf4j;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class AlarmUtil {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String alarmLogic(String tableName, Map<String, Object> historyStatus, Map<String, Object> currentStatus,List<String> Openids,String factoryName,Map<String, Object> alarmInfo) throws ParseException {

        // 1、初始化 alarmNameList、ff、sf 数组、alarmTime
        Map<String, Object> items = initTemp(historyStatus, currentStatus);

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
                if (ScheduledTasks.reference.containsKey(tableName)) {
                    if (ScheduledTasks.reference.get(tableName).containsKey(alarmName)) {
                        String ALARMTIME = ScheduledTasks.reference.get(tableName).get(alarmName);
                        if (ALARMTIME.contains("报警时间不详")){
                            String realALARMTIME = ALARMTIME.substring(0,ALARMTIME.indexOf("报"));
                            Long past  = dateFormat.parse(realALARMTIME).getTime();//java.text.ParseException: Unparseable date: "报警时间不详!"
                            Long now = dateFormat.parse(alarmTime).getTime();
                            if ((now - past)!=0 && ((now - past) % (1000 * 60 * 2) == 0)){
                                String contentName = getContentName(alarmName);
                                String content = String.valueOf(alarmInfo.get(contentName));
                                sendAlarmMsg(factoryName, "报警时间不详!", content, Openids);
                            }
                        }
                        else {
                            Long past  = dateFormat.parse(ALARMTIME).getTime();//java.text.ParseException: Unparseable date: "报警时间不详!"
                            Long now = dateFormat.parse(alarmTime).getTime();
                            if (((now - past) % (1000 * 60 * 2) == 0)) {//(now - past)!=0
                                String contentName = getContentName(alarmName);
                                String content = String.valueOf(alarmInfo.get(contentName));
                                System.err.println(factoryName + "：报警位" + alarmName + ",报警内容content:" + content);
                                sendAlarmMsg(factoryName, alarmTime, content, Openids);
                            }
                        }

                    } else {
                        Map<String, String> map = new LinkedHashMap<>();
                        map.put(alarmName, alarmTime);
                        ScheduledTasks.reference.put(tableName, map);
                    }
                } else {
                    Map<String, String> map = new LinkedHashMap<>();
                    map.put(alarmName, alarmTime);
                    ScheduledTasks.reference.put(tableName, map);
                }
            }
            else if (one || two) {
                if (one) {//1->0
                    ScheduledTasks.reference.get(tableName).remove(alarmName);
                }
                else {//0->1
                    String contentName = getContentName(alarmName);
                    String content = String.valueOf(alarmInfo.get(contentName));
                    log.info(factoryName+"：报警位"+alarmName+",报警内容content:"+content);
                    sendAlarmMsg(factoryName,alarmTime,content,Openids);
                    //记录xx表的xx时间xx字段报警，以便1-1的时间判断
                    Map<String,String> map = new LinkedHashMap<>();
                    map.put(alarmName,alarmTime);
                    ScheduledTasks.reference.put(tableName,map);
                }
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
//        System.err.println("res:"+res);
        return  res;
    }


    /**
     * 用报警字段，拼接报警内容字段
     * @param alarmName
     * @return
     */
    public static String getContentName(String alarmName){
        String number = alarmName.substring(alarmName.lastIndexOf("m")+1, alarmName.length());
        Integer temp = Integer.valueOf(number);
        if(temp<10){
            number = temp.toString();
        }
        return "alarm"+number+"_content";
    }


    /**
     * 初始化 alarmNameList、ff、sf 数组，避免代码重复
     * @param historyStatus
     * @param currentStatus
     * @return
     */
    public static  Map<String,Object> initTemp(Map<String, Object> historyStatus, Map<String, Object> currentStatus){
        List<String> alarmNameList = new ArrayList<>();
        List<Boolean> ff = new ArrayList<>();
        List<Boolean> sf = new ArrayList<>();
        String alarmTime = null;
        // 1 初始化 alarmNameList、ff、sf 数组
        for (Map.Entry<String, Object> historyMeta : historyStatus.entrySet()) {
            if ("TIME".equals(historyMeta.getKey())) {//获取time
                alarmTime = dateFormat.format(historyMeta.getValue());
            }
            else{
                alarmNameList.add(historyMeta.getKey());
                ff.add((Boolean) historyMeta.getValue());
            }
        }
        for (Map.Entry<String, Object> currentMeta : currentStatus.entrySet()) {
            if (!"TIME".equals(currentMeta.getKey())) {//去掉time的后的map，全是报警位
                sf.add((Boolean) currentMeta.getValue());
            }
        }
        Map<String,Object> res = new LinkedHashMap<>();
        res.put("alarmNameList",alarmNameList);
        res.put("ff",ff);
        res.put("sf",sf);
        res.put("alarmTime",alarmTime);
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
    public static void initReference(String tableName, Map<String, Object> historyStatus, Map<String, Object> currentStatus){
        // 1、初始化 alarmNameList、ff、sf 数组、alarmTime
        Map<String, Object> items = initTemp(historyStatus, currentStatus);

        List<String> alarmNameList = (List<String>)items.get("alarmNameList");
        List<Boolean> ff = (List<Boolean>)items.get("ff");
        List<Boolean> sf = (List<Boolean>)items.get("sf");
        String alarmTime = String.valueOf(items.get("alarmTime"));

        // 2、初始化 reference map
//        Map<String,Map<String,String>> reference = new LinkedHashMap<>();

        for (int i = 0; i < ff.size(); i++) {//初始化reference
            Boolean one = ff.get(i);
            Boolean two = sf.get(i);
            String alarmName = alarmNameList.get(i);
            if (one && two) {//1->1
                if (ScheduledTasks.reference.containsKey(tableName)) {
                    if (ScheduledTasks.reference.get(tableName).containsKey(alarmName)) {

                    }
                    else {
                        ScheduledTasks.reference.get(tableName).put(alarmName,alarmTime+"报警时间不详");
                    }
                }
                else {
                    Map<String, String> map = new LinkedHashMap<>();
                    map.put(alarmName, alarmTime+"报警时间不详");
                    ScheduledTasks.reference.put(tableName, map);
                }
            }
            else if (one || two) {
                if (one) {//1->0
                    ScheduledTasks.reference.get(tableName).remove(alarmName);
                }
                else {//0->1
                    if (ScheduledTasks.reference.containsKey(tableName)) {
                        ScheduledTasks.reference.get(tableName).put(alarmName, alarmTime);
                    }
                    else {
                        Map<String, String> map = new LinkedHashMap<>();
                        map.put(alarmName, alarmTime);
                        ScheduledTasks.reference.put(tableName, map);
                    }
                }
            }
        }

    }
}
