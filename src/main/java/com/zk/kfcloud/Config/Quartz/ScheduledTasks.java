package com.zk.kfcloud.Config.Quartz;

import java.text.ParseException;
import java.util.*;

import com.zk.kfcloud.Entity.web.UserInfo;
import com.zk.kfcloud.Entity.web.WeChat;
import com.zk.kfcloud.Service.FactoryService;
import com.zk.kfcloud.Service.MenuService;
import com.zk.kfcloud.Service.WeChatService;
import com.zk.kfcloud.Utils.AlarmUtil;
import com.zk.kfcloud.Utils.wechat.Authorization;
import com.zk.kfcloud.Utils.wechat.MaterialManage;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ScheduledTasks {

    @Autowired
    private WeChatService weChatService;

    public static  List<Map<String, Object>> historyStatusList1 = new ArrayList<>();
    public static  List<String> alarmTableList = new ArrayList<>();
    public static Map<String,Map<String,String>> reference1 = new LinkedHashMap<>();
    public static  List<Map<String, Object>> historyStatusList2 = new ArrayList<>();
    public static Map<String,Map<String,String>> reference2 = new LinkedHashMap<>();
    @Autowired
    FactoryService factoryService;

    @Autowired
    MenuService menuService;

    // 1、存放要监控的的表
    static {
        alarmTableList.add("KF0001");
        alarmTableList.add("KF0002");
        alarmTableList.add("KF0004");
        alarmTableList.add("KF0005");
    }

    /*
    *每日获取用户信息存入数据库
     */
    @Scheduled(fixedRate = 1000*60*60*6)
    public void test() throws ParseException {
        String access_token = MaterialManage.getAccessToken().getAccess_token();
        List<String> userOpenids=Authorization.getallopenId( access_token,"");
        List user_list = new ArrayList<Map>();
         for (int i = 0; i < userOpenids.size(); i++)
          { String openid = userOpenids.get(i);
            Map tUserMap = new HashMap<String, String>();
            tUserMap.put("openid", openid);
            tUserMap.put("lang", "zh_CN");
            user_list.add(tUserMap);
          }
        Map requestMap = new HashMap<String, List>();
        requestMap.put("user_list", user_list);
        String tUserJSON = JSONObject.fromObject(requestMap).toString();
        String result=Authorization.getUserInfobyOpenIds(access_token,tUserJSON);
        List<UserInfo> user_info_list=Authorization.parseUserInfo(result);
        log.info("result:"+result);
        for (int i = 0; i < user_info_list.size(); i++)
        {   UserInfo userinfo=user_info_list.get(i);
            List<WeChat> allWeChatUser = weChatService.findAllWeChatUser();
            Boolean flag = false;
            for (WeChat wxUser : allWeChatUser)
            {
                if ((userinfo.getOpenId()).equals(wxUser.getOpenId())) {//用户openid存在，插入数据库wechar_name
                    flag = true;
                }
            }
            if (flag) {
                WeChat wx = new WeChat();
                wx.setOpenId(userinfo.getOpenId());
                String username=Authorization.filterEmoji(userinfo.getNickname());//过滤掉emoji表情符，统一用*代替
                wx.setName(username);
                log.info("userinfo.getOpenId():"+userinfo.getOpenId());
                log.info("userinfo.getNickname():"+userinfo.getNickname());
                weChatService.updateWecharname(wx);
            }
        }
    }
    /**
     * 生产数据监控
     * @throws ParseException
     */
    @Scheduled(fixedRate = 1000*10)
    public void reportAlarmStatus() throws ParseException {
        for (int i=0; i<alarmTableList.size();i++){
            if (historyStatusList1.size() == i){
                historyStatusList1.add(i,factoryService.monitor(alarmTableList.get(i)));//初始化所有表的报警信息
            }
        }
        // 3、分别查询出当前数据表的最新数据，判断是否需要报警
        List<String> factoryNames = factoryService.returnfactoryNames(alarmTableList);//获得厂名
        log.info("factoryNames:"+factoryNames);
        for (int i=0; i<alarmTableList.size();i++){
            Map<String, Object> historyStatus = historyStatusList1.get(i);//获取历史报警信息
            Map<String, Object> currentStatus = factoryService.monitor(alarmTableList.get(i));//获取当前的报警信息
            String tableName = alarmTableList.get(i);
            // 初始化 alarmNameList、ff、sf 数组、alarmTime、reference
            AlarmUtil.initReference1(tableName, historyStatus, currentStatus);
            log.info("报警reference里："+reference1);//报警信息
            // 获取关注该厂的所有微信用户
            Map<String, Object> alarmInfo = factoryService.getAlarmInfoByAlarmUrl(tableName.substring(0,6));
            List<String> realOpenIds = factoryService.getOpenids(tableName);
            log.info("生产报警发送openid:"+realOpenIds);
            // 报警逻辑部分
            String factoryName = factoryNames.get(i);
            AlarmUtil.alarmLogic1(tableName,historyStatus,currentStatus,realOpenIds,factoryName,alarmInfo);
        }
        // 4、清空一下historyStatusList，并将currentStatus复制给historyStatus（historyStatusList.get(i)）
            historyStatusList1.clear();
        for (int i=0; i<alarmTableList.size();i++){
            Map<String, Object> currentStatus = factoryService.monitor(alarmTableList.get(i));
            historyStatusList1.add(currentStatus);
        }
    }


    /**
     * 分析数据监控
     * @throws ParseException
     */
    @Scheduled(fixedRate = 1000*10)
    public void reportAlarmStatus2() throws ParseException {
        // 1、查出数据表对应的工厂名称
        List<String> factoryNames = factoryService.returnfactoryNames(alarmTableList);
        // 2、执行监控逻辑
        for (int i=0; i<alarmTableList.size();i++){
            if (historyStatusList2.size() == i){
                historyStatusList2.add(i,factoryService.anlAlarmLogic(alarmTableList.get(i)));//初始化所有表的报警信息
            }
        }
        for (int i=0; i<alarmTableList.size();i++){
            // 2.1、初始化数据
            String tableName = alarmTableList.get(i);
            String factoryName = factoryNames.get(i);
            Map<String, Object> currentStatus = factoryService.anlAlarmLogic(tableName);//获取当前数据
            log.info(factoryName+"当前指标报警数据:"+currentStatus);
            Map<String, Object> historyStatus = historyStatusList2.get(i);//获取历史报警信息
            log.info(factoryName+"前一时刻指标报警数据:"+historyStatus);
            Map<String, Object> alarmInfo = factoryService.anlAlarmLogic(tableName.substring(0,6));
            List<String> realOpenIds = factoryService.getOpenids(tableName);
//            List<String> realOpenIds = new ArrayList();
//            realOpenIds.add("osAgr1Eoe3jZu74qEve0b1_d6e7Y");

            log.info("指标报警发送openid:"+realOpenIds);

            // 2.2 主逻辑部分
            AlarmUtil.alarmLogic2(tableName,historyStatus,currentStatus,realOpenIds,factoryName,alarmInfo);
        }
        //清空历史记录储存当前信息
        historyStatusList2.clear();
        for (int i=0; i<alarmTableList.size();i++){
            Map<String, Object> currentStatus = factoryService.anlAlarmLogic(alarmTableList.get(i));
            historyStatusList2.add(currentStatus);
        }
    }

}
