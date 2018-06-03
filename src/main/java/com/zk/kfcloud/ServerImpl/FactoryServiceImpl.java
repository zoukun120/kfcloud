package com.zk.kfcloud.ServerImpl;


import java.io.IOException;
import java.util.*;

//import com.zk.kfcloud.Config.Quartz.AlarmJob;
//import com.zk.kfcloud.Config.Quartz.QuartzConfigration;
import ch.qos.logback.core.db.dialect.SybaseSqlAnywhereDialect;
import com.zk.kfcloud.Dao.FactoryMapper;
import com.zk.kfcloud.Entity.web.Factory;
import com.zk.kfcloud.Entity.web.Menu;
import com.zk.kfcloud.Service.FactoryService;
import com.zk.kfcloud.Service.MenuService;
import com.zk.kfcloud.Utils.Tools;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FactoryServiceImpl implements FactoryService {

	@Autowired
	FactoryMapper factoryMapper;

	@Autowired
	MenuService menuService;


	@Override
	public List<String> getOpenids(String tableName) {
		tableName = tableName.substring(0,6);
		Map<String, Object> alarms = factoryMapper.getAlarmInfoByAlarmUrl(tableName);
		Integer alarmId = (Integer) alarms.get("alarm_id");
		List<Integer> factoryIds = factoryMapper.getFactoryIdsByAlarmId(alarmId);
		Map<String,List<String>> result = new LinkedHashMap<>();
		List<String> openIDS = new ArrayList<>();
		for (int i = 0; i < factoryIds.size(); i++) {
			List<Integer> menuIds = factoryMapper.getMenuIdsByFactoryId(factoryIds.get(i));
			Integer menuId = menuIds.get(0);
			Menu menu = menuService.getMenuById(menuId);
			List<String> openIds = factoryMapper.getOpenIdsByMenuId(menuId);
			for (int j = 0; j < openIds.size(); j++) {
				openIDS.add(openIds.get(j));
			}
		}
		return openIDS;
	}

	@Override
	public List<String> getFactoryNames(String tableName) {
		Integer alarmId = (Integer) factoryMapper.getAlarmInfoByAlarmUrl(tableName).get("alarm_id");
		List<Integer> factoryIds = factoryMapper.getFactoryIdsByAlarmId(alarmId);
//		log.info("factoryIds:"+factoryIds);
		List<String> factoryNames = new ArrayList<>();
		for (int i = 0; i < factoryIds.size(); i++) {
			List<Integer> menuIds = factoryMapper.getMenuIdsByFactoryId(factoryIds.get(i));
//			System.err.println("menuIds:"+menuIds);
			Menu menu = menuService.getMenuById(menuIds.get(0));
			factoryNames.add(menu.getMenuName());
		}
		return factoryNames;
	}

	@Override
	public Map<String, Object> getAlarmInfoByAlarmUrl(String alarmTableName) {
		return factoryMapper.getAlarmInfoByAlarmUrl(alarmTableName);
	}


//	private QuartzConfigration schedulerFactoryConfig;

	public List<Factory> listAllInfoByFactoryId(Integer factoryId) {
		return this.factoryMapper.listAllInfoByFactoryId(factoryId);
	}

	public Map<String, Object> getData(String KFTable, String SqlFields) {
		return this.factoryMapper.getData(KFTable, SqlFields);
	}

	public Map<String, Object> getAlarmInfoByAlarmId(Integer alarmId) {
		return this.factoryMapper.getAlarmInfoByAlarmId(alarmId);
	}

	public void insertData() {
		this.factoryMapper.insertData();
	}

	public List<String> listAllSystemNameByFactoryId(Integer factoryId) {
		return this.factoryMapper.listAllSystemNameByFactoryId(factoryId);
	}

	public Map<String, Object> getAlarmData(String AlarmTable) {
		return this.factoryMapper.getAlarmData(AlarmTable);
	}

	public int getParaNum(String modelName, Integer modelId) {
		Map<String, Object> map = new HashMap<>();
		map.put("modelName", modelName);
		map.put("modelId", modelId);
		return this.factoryMapper.getParaNum(map);
	}

	public Map<String, Object> getParaValues(String modelName, Integer modelId, String fields) {
//		log.info("fields:" + fields);
		Map<String, Object> map = new HashMap<>();
		map.put("modelName", modelName);
		map.put("modelId", modelId);
		map.put("fields", fields);
		return this.factoryMapper.getParaValues(map);
	}

	public Map<String, Object> getParasByModelNameAndId(String modelName, Integer modelId) {
		Map<String, Object> map = new HashMap<>();
		map.put("modelName", modelName);
		map.put("modelId", modelId);
		return this.factoryMapper.getParasByModelNameAndId(map);
	}

	public List<Object> getHistoryDatasByDate(Map<String, Object> dateMap, String tableName, String KFFields) {
		dateMap.put("tableName", tableName);
		dateMap.put("KFFields", KFFields);
		return this.factoryMapper.getHistoryDatasByDate(dateMap);
	}
	
	public List<Object> getHistoryDatasByDate2(Map<String, Object> dateMap, String tableName, String KFFields,String yearmonth) {
		dateMap.put("tableName", tableName);
		dateMap.put("KFFields", KFFields);
		dateMap.put("yearmonth", yearmonth);
		return this.factoryMapper.getHistoryDatasByDate2(dateMap);
	}

	public int getAlarmIdByFactoryId(Integer factoryId) {
		return this.factoryMapper.getAlarmIdByFactoryId(factoryId);
	}

	@Override
	public Map<String, Object> getParaAnalysisData(Integer factoryId) {
		// TODO Auto-generated method stub
		return factoryMapper.getParaAnalysisData(factoryId);
	}

	@Override
	public Map<String, Object> getDailyData(Integer teamNum, String tableName) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<>();
		map.put("teamNum", teamNum);
		map.put("tableName", tableName);
		return factoryMapper.getDailyData(map);
	}

	@Override
	public List<Object> getNewestStateData(String tableName) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<>();
		map.put("tableName", tableName);
		return factoryMapper.getNewestStateData(map);
	}
	
	@Override
	public List<Object> getAllData(Map<String, Object> dateMap,String tableName){
		// TODO Auto-generated method stub
		dateMap.put("tableName", tableName);
		return factoryMapper.getAllData(dateMap);
	}

	@Override
	public List<Menu> commonCode(Integer userid) {
		List<Menu> menus = new ArrayList<>();
		List<Integer> menuIds = menuService.getMenuIdByUserId(userid);
		for (Integer menuId:menuIds) {
			Menu menu = menuService.getMenuById(menuId);
			if (Tools.isEmpty(menu.getMenuUrl())){
				menu.setSubMenu(menuService.listSubMenuByParentId(menuId));
			}
			menus.add(menu);
		}
//		for (Menu menu:menus) {
//			log.info(menu.toString());
//		}
		return menus;
	}

	@Override
	public String getDashBoardTableName(Integer factoryId) {
		return factoryMapper.getDashBoardTableName(factoryId);
	}

	@Override
	public Map<String, Object> monitor(String tableName) {
		Map<String, Object> alarmData = factoryMapper.getAlarmData(tableName);
		String feilds = "TIME,";
		for (Map.Entry<String,Object> alarm:alarmData.entrySet()) {
			String key = alarm.getKey();
			if(key.contains("Alarm")){
				feilds += key+",";
			}

		}
		feilds = feilds.substring(0,feilds.lastIndexOf(","));

		Map<String, Object> alarmMap = factoryMapper.getData(tableName, feilds);
//		log.info("查询报警表："+tableName+",查询字段"+feilds);
		return  alarmMap;
	}

//	@Override
//	public String testQuartz() throws SchedulerException {
//		//定时器
//		JobDetail jobDetail = JobBuilder
//				.newJob(AlarmJob.class)
//				.withIdentity("myJob", "jonGroup1")
//				.build();
//
//		CronTrigger trigger = (CronTrigger) TriggerBuilder.newTrigger()
//				.withIdentity("myTrigger","triggerGroup1")
//				.withSchedule(CronScheduleBuilder.cronSchedule("*/2 * * * * ? *"))
//				.build();
//
//		Scheduler scheduler = null;
//		try {
//			scheduler = schedulerFactoryConfig.schedulerFactoryBean().getScheduler();
//		}catch(IOException e){
//			e.printStackTrace();
//		}
//		scheduler.scheduleJob(jobDetail, trigger);
//		scheduler.start();
//
//		return "quartz 启动成功！";
//	}
}
