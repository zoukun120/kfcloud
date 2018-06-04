package com.zk.kfcloud.Service;

import com.zk.kfcloud.Entity.web.Factory;
import com.zk.kfcloud.Entity.web.Menu;
import org.quartz.SchedulerException;

import java.util.List;
import java.util.Map;

public abstract interface FactoryService {
	
	public abstract List<Factory> listAllInfoByFactoryId(Integer paramInteger);

	public abstract int getAlarmIdByFactoryId(Integer paramInteger);

	public abstract List<String> listAllSystemNameByFactoryId(Integer paramInteger);

	public abstract Map<String, Object> getData(String paramString1, String paramString2);

	public abstract Map<String, Object> getAlarmInfoByAlarmId(Integer paramInteger);

	public abstract Map<String, Object> getAlarmData(String paramString);

	public abstract Map<String, Object> getParasByModelNameAndId(String paramString, Integer paramInteger);

	public abstract int getParaNum(String paramString, Integer paramInteger);

	public abstract Map<String, Object> getParaValues(String paramString1, Integer paramInteger, String paramString2);

	public abstract List<Object> getHistoryDatasByDate(Map<String, Object> paramMap, String paramString1,
                                                       String paramString2);
	
	public abstract List<Object> getHistoryDatasByDate2(Map<String, Object> paramMap, String paramString1,
                                                        String paramString2, String yearmonth);
	
	public abstract List<Object> getNewestStateData(String tableName);

	public abstract void insertData();
	
	public abstract Map<String, Object> getParaAnalysisData(Integer factoryId);
	
	public abstract Map<String, Object> getDailyData(Integer teamNum, String tableName);

	List<Object> getAllData(Map<String, Object> dateMap, String tableName);

	public abstract List<Menu> commonCode(Integer userid);

	String getDashBoardTableName(Integer factoryId);

//	String testQuartz() throws SchedulerException;

	Map<String, Object> monitor(String tableName);

	List<String> getOpenids(String tableName);

	List<String> getFactoryNames(String tableName);

	Map<String, Object> getAlarmInfoByAlarmUrl(String alarmTableName);

	/**
	 * 分析数据逻辑
	 * @param tableName
	 * @return
	 */
	Map<String,Object> anlAlarmLogic(String tableName);

	List<String> returnfactoryNames(List<String> alarmTableList);

}
