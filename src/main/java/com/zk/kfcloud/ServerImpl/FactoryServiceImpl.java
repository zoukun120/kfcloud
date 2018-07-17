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
				if(factoryMapper.getalarm_authByOpenId(openIds.get(j))==null)
				{
					openIDS.add(openIds.get(j));
				}
				else if (factoryMapper.getalarm_authByOpenId(openIds.get(j))==1) {
					openIDS.add(openIds.get(j));
				}
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

	/**
	 * 两个定时任务都会用到此方法，故封装起来，避免代码重复
	 * @param alarmTableList
	 * @return
	 */
	@Override
	public List<String> returnfactoryNames(List<String> alarmTableList) {
		List<String> factoryNames = new ArrayList<>();
		for (int i = 0; i < alarmTableList.size(); i++) {
			String tabeleName = alarmTableList.get(i);
			tabeleName = tabeleName.substring(0,6);
			List<String> factoryNames1 = getFactoryNames(tabeleName);
			for (int j = 0; j < factoryNames1.size(); j++) {
				factoryNames.add(factoryNames1.get(j));
			}
		}
		return factoryNames;
	}

	@Override
	public Map<String, Object> anlAlarmLogic(String tableName) {
		tableName = tableName.substring(0,6)+"_ana_sec";
		return factoryMapper.getData(tableName,"*");//测试取getData1为升序，实用取getData为降序//!!!!!!!!!!!!!
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
		return menus;
	}
	@Override
	public Integer AlarmIndex(String openid ) {
		Integer stateValue;
		stateValue = menuService.getAlarm_authByOpenId(openid);

		return stateValue;
	}

	@Override
	public String getDashBoardTableName(Integer factoryId) {
		return factoryMapper.getDashBoardTableName(factoryId);
	}

	/**
	 * 从tb2_model2中读取xxx表需要监控的报警字段
	 * @param tableName
	 * @return
	 */
	@Override
	public Map<String, Object> monitor(String tableName) {
		String TableName = tableName.substring(0,6);//测试阶段取前6位
		Map<String, Object> alarmData = factoryMapper.getAlarmInfoByAlarmUrl(TableName);//获取表名的相关报警信息
		String feilds = "TIME,";
		for (Map.Entry<String,Object> alarm:alarmData.entrySet()) {
			String key = alarm.getKey();//取键值
			if(key.contains("name")){
				feilds += alarm.getValue()+",";//获取表存在的报警名称
			}

		}
		feilds = feilds.substring(0,feilds.lastIndexOf(","));
		Map<String, Object> alarmMap = factoryMapper.getData(tableName, feilds);//获取报警标志位
//		log.info("查询报警表："+tableName+",查询字段"+feilds);
		return  alarmMap;

	}

}
