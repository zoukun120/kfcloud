package com.zk.kfcloud.Dao;

import java.util.List;
import java.util.Map;

import com.zk.kfcloud.Entity.web.Factory;
import org.apache.ibatis.annotations.Param;

public interface FactoryMapper{
	
  public abstract List<Factory> listAllInfoByFactoryId(Integer paramInteger);
  
  public abstract int getAlarmIdByFactoryId(Integer paramInteger);
  
  public abstract List<String> listAllSystemNameByFactoryId(Integer paramInteger);
  
  public abstract Map<String, Object> getData(@Param("KFTable") String paramString1, @Param("SqlFields") String paramString2);

  public abstract Map<String, Object> getallDatabyFactoryId(@Param("TableName") String TableName, @Param("FactoryId") Integer FactoryId);

  public abstract Map<String, Object> getData1(@Param("KFTable") String paramString1, @Param("SqlFields") String paramString2);

  public abstract Map<String, Object> getAlarmInfoByAlarmId(Integer paramInteger);
  
  public abstract Map<String, Object> getAlarmData(@Param("AlarmTable") String paramString);
  
  public abstract Map<String, Object> getParasByModelNameAndId(Map<String, Object> paramMap);
  
  public abstract int getParaNum(Map<String, Object> paramMap);
  
  public abstract Map<String, Object> getParaValues(Map<String, Object> paramMap);
  
  public abstract List<Object> getHistoryDatasByDate(Map<String, Object> paramMap);
  
  public abstract List<Object> getHistoryDatasByDate2(Map<String, Object> paramMap);
  
  public abstract List<Object> getNewestStateData(Map<String, Object> paramMap);
  
  public abstract void insertData();
  
  public abstract Map<String, Object> getParaAnalysisData(Integer factoryId);
  
  public abstract Map<String, Object> getDailyData(Map<String, Object> paramMap);
  
  public abstract List<Object> getAllData(Map<String, Object> dateMap);

  public abstract String getDashBoardTableName(Integer factoryId);

  public abstract Map<String, Object> getAlarmInfoByAlarmUrl(String alarmTableName);

  public abstract List<Integer> getFactoryIdsByAlarmId(Integer alarmId);

  public abstract List<Integer> getMenuIdsByFactoryId(Integer factoryId);

  public abstract List<String> getOpenIdsByMenuId(Integer menuId);

  public abstract Integer getalarm_authByOpenId(String openId);

  public abstract String getAlarmtimeonByOpenId(String openId);

}
