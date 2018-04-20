package com.zk.kfcloud.Controller;

import com.zk.kfcloud.Entity.web.Factory;
import com.zk.kfcloud.Service.FactoryService;
import com.zk.kfcloud.Utils.DateFilter;
import com.zk.kfcloud.Utils.FactoryUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@Controller
public class FactoryController {

    @Autowired
    private FactoryService factoryService;

    @GetMapping("/subMenu/{pagename}/{factoryId}")
    public String sendPage(@PathVariable("pagename") String pagename, @PathVariable("factoryId") String factoryId, Model model) {
        model.addAttribute("factoryId", factoryId);
        //        去掉后缀'.html'
        return pagename.substring(0, pagename.indexOf("."));
    }

    @GetMapping("/sys")
    public String sys() {
        return "current";
    }

    /**
     * 返回某工厂current页面拥有的空分系统数据
     *
     * @param id
     * @return
     */

    @GetMapping("/current/{factoryId}")
    @ResponseBody
    public List<Factory> curSystem(@PathVariable("factoryId") Integer id) {

        return factoryService.listAllInfoByFactoryId(id);
    }

    @GetMapping("/current/{modelName}/{modelId}/{systemName}")
    @ResponseBody
    public JSONObject curSystem(@PathVariable("modelName") String modelname, @PathVariable("modelId") Integer id, @PathVariable("systemName") String systemname) {
        System.err.println(modelname);
        System.err.println(id);
        System.err.println(systemname);
//      1 在表tb2_model1中，查出对应id的信息
        Map<String, Object> paraMap = factoryService.getParasByModelNameAndId(modelname, id);
        System.err.println(paraMap);
//          1.1 得到数据表 如KF0004
        String KFTable = (String) paraMap.get("para_url");
//          1.2 遍历paraMap，得到sql的’查询字符串‘
        StringBuilder KFFields = new StringBuilder();
        for (Map.Entry<String, Object> str : paraMap.entrySet()) {
            String key = (String) str.getKey();
            if ((!"".equals(str.getValue())) && (str.getValue() != null) && (!"image_name".equals(key))
                    && (key.contains("name"))) {
                String value = (String) str.getValue();
                KFFields.append(value + ",");
            }
        }
        String SqlFields = KFFields.substring(0, KFFields.toString().lastIndexOf(","));
        log.info("KFTable:" + KFTable);
        log.info("SqlFields:" + SqlFields);

//      2 查询KFTable（KF0004）中最新一条SqlFields（para_num.para_name,para_suffix...）数据
        Object dataMap = factoryService.getData(KFTable, SqlFields);

//      3 返回json数据
        JSONObject json = new JSONObject();
        json.put("paraMap", paraMap);
        json.put("dataMap", dataMap);
        return json;
    }

    /**
     * 返回某工厂history页面拥有的空分系统数据
     *
     * @param id
     * @return
     */

    @GetMapping("/history/{factoryId}")
    @ResponseBody
    public List<Factory> hisSystem(@PathVariable("factoryId") Integer id) {
        return factoryService.listAllInfoByFactoryId(id);
    }

    @PostMapping("/history")
    @ResponseBody
    public JSONObject hisData(@RequestBody Map<String, Object> map) throws Exception {
        String modelName =  String.valueOf(map.get("modelName"));
        Integer modelId =  Integer.valueOf(String.valueOf(map.get("modelId")));
        String dateStart =  String.valueOf(map.get("dateStart"));
        String dateEnd =  String.valueOf(map.get("dateEnd"));
        System.err.println("modelName:"+modelName+",modelId:"+modelId+",dateStart:"+dateStart+",dateEnd:"+dateEnd);
//        1 从dateStart:2017-11-15 15:00:00  中取出2017-11传入sql，用来动态选择指定月份某一表（如 KF0001_201711）
        String year = dateStart.substring(0, dateStart.indexOf("-"));
        String month = dateStart.substring(dateStart.indexOf("-")+1, dateStart.lastIndexOf("-"));
        if(month.length()==1) {
            month = "0"+month;
        }
        System.out.println(year+","+month);
//        2 组装tb2_model的查询字符串
        int paraNum = factoryService.getParaNum(modelName, modelId);
        String ModelFields = FactoryUtil.assemblyModelField(paraNum, "para_url,para_num,");
        log.info("paraNum:" + paraNum + ",ModelFields:" + ModelFields);

//        3 组装kf_000x 的查询字符串
        Map<String, Object> paraMap = factoryService.getParaValues(modelName, modelId, ModelFields);
        String KFFields = FactoryUtil.assemblyKfField(paraMap, "TIME,");
        log.info("KFFields:" + KFFields);

//        4 组装要查询的空分表（kf_000x月日）
        Map<String, Object> dateMap = DateFilter.dateFilter(dateStart, dateEnd);
        String tableName = (String) paraMap.get("para_url");

        Calendar now = Calendar.getInstance();//实现分月查询
        int nowyear = now.get(Calendar.YEAR);
        int nowmonth = now.get(Calendar.MONTH)+1;
        System.out.println("当下年月："+nowyear+","+month);
        if(Integer.valueOf(year)!=nowyear || Integer.valueOf(month)!=nowmonth) {
            tableName = tableName + "_"+year+month;
        }
        System.out.println("查询的表："+tableName);

//        5 查询历史数据
        List<Object> hisDataList = new ArrayList<>();
        try {//数据库没有当月的表，会报错，捕获异常，返回页面空值
            hisDataList = factoryService.getHistoryDatasByDate(dateMap, tableName, KFFields);
        } catch (Exception e) {
            hisDataList = null;
        }
        JSONObject json = new JSONObject();
        json.put("paraMap", paraMap);
        json.put("hisDataList", hisDataList);
//        Map<String, Object> result = new HashMap<>();
//        result.put("paraMap", paraMap);
//        result.put("hisDataList", hisDataList);
        return json;
    }
}
