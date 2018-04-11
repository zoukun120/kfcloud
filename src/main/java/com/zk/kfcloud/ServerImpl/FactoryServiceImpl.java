package com.zk.kfcloud.ServerImpl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zk.kfcloud.Dao.FactoryMapper;
import com.zk.kfcloud.Entity.web.Factory;
import com.zk.kfcloud.Service.FactoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FactoryServiceImpl implements FactoryService {
	@Autowired
	FactoryMapper factoryMapper;

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
		log.info("fields:" + fields);
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
	
}