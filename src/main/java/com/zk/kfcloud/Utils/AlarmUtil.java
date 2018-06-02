package com.zk.kfcloud.Utils;

import com.zk.kfcloud.Service.FactoryService;
import com.zk.kfcloud.Utils.wechat.Authorization;
import com.zk.kfcloud.Utils.wechat.MaterialManage;
import com.zk.kfcloud.Utils.wechat.Template;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class AlarmUtil {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    FactoryService factoryService;

    public static String alarmLogic(String tableName, Map<String, Object> historyStatus, Map<String, Object> currentStatus,List<String> Openids,String factoryName,Map<String, Object> alarmInfo) throws ParseException {

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
        log.info("ff大小" + ff.size() + "->" + ff);
        log.info("sf大小" + sf.size() + "->" + sf);

        // 2、报警逻辑判断
        for (int i = 0; i < ff.size(); i++) {
            Boolean one = ff.get(i);
            Boolean two = sf.get(i);
            String alarmName = alarmNameList.get(i);
            // 0->0 正常
            // 0->1 报警一次,如xxx表，什么字段，报警内容
            // 1->0 恢复正常
            // 1->1 此报警状态若维持5分钟以上就在此触发报警
            if (one && two) {//1->1
//                String contentName = getContentName(alarmName);
//                String content = String.valueOf(alarmInfo.get(contentName));
//                System.err.println(factoryName+"：报警位"+alarmName+",报警内容content:"+content);
//                sendAlarmMsg(factoryName,alarmTime,content,Openids);
            }
            else if (one || two) {
                if (one) {//1->0
                    System.err.println(tableName + "表-" + alarmName + "（1->0） 恢复正常");
                }
                else {//0->1
                    String contentName = getContentName(alarmName);
                    String content = String.valueOf(alarmInfo.get(contentName));
                    System.err.println(factoryName+"：报警位"+alarmName+",报警内容content:"+content);
                    sendAlarmMsg(factoryName,alarmTime,content,Openids);
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

}
