package com.zk.kfcloud.Controller;

import com.zk.kfcloud.Entity.web.Analysis;
import com.zk.kfcloud.Entity.web.Factory;
import com.zk.kfcloud.Service.FactoryService;
import com.zk.kfcloud.Utils.DateFilter;
import com.zk.kfcloud.Utils.DateJsonValueProcessor;
import com.zk.kfcloud.Utils.FactoryUtil;
import com.zk.kfcloud.Utils.Tools;
import com.zk.kfcloud.Utils.wechat.Template;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Controller
public class FactoryController {

    @Autowired
    private FactoryService factoryService;

//    @GetMapping("/startQuartz")
//    public @ResponseBody String openQuartz() throws SchedulerException {
//        System.err.println("factoryService.testQuartz()");
//        String testQuartz = factoryService.testQuartz();
//        System.err.println(testQuartz);
//        return testQuartz;
//    }

    @GetMapping("/subMenu/{pagename}/{factoryId}")
    public String sendPage(@PathVariable("pagename") String pagename, @PathVariable("factoryId") String factoryId, Model model) {
        model.addAttribute("factoryId", factoryId);
        //        去掉后缀'.html'
        System.err.println(pagename);
        return pagename;
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
//        System.err.println(modelname);
//        System.err.println(id);
//        System.err.println(systemname);
//      1 在表tb2_model1中，查出对应id的信息
        Map<String, Object> paraMap = factoryService.getParasByModelNameAndId(modelname, id);
//        System.err.println(paraMap);
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
        String SqlFields = "TIME,"+KFFields.substring(0, KFFields.toString().lastIndexOf(","));
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
//        log.info("paraNum:" + paraNum + ",ModelFields:" + ModelFields);

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
        return json;
    }

    /**
     * 测试分析页面：ajax代替iframe
     * @return
     */
    @GetMapping("analysis")
    public String analysis(){
        return "analysis";
    }

    @GetMapping("/anal/{pageName}/{factoryId}")
    public String toAnalysisPage(@PathVariable("pageName") String name,@PathVariable("factoryId") Integer id,Model model){
        System.err.println("页面"+name+"的factoryId是 "+id);
        model.addAttribute("factoryId", id);
        return name;
    }

    /**
     * 报表数据
     * @param analysis
     * @return
     */
    @PostMapping("/anal/dailyReport/table")
    @ResponseBody
    public JSONArray table(@RequestBody Analysis analysis){
        System.err.println(analysis);
        Integer factoryId = analysis.getFactoryId();
        String dateFrist = analysis.getDateFrist();
        String dateEnd = analysis.getDateEnd();
        // 1 根据factoryId 获取para_ana1表的数据
        Map<String, Object> paraAnalysisData = factoryService.getParaAnalysisData(factoryId);
        // 2 根据班组编号查询数据，并传入动态表名
        String out_day_tableName = String.valueOf(paraAnalysisData.get("out_table_day"));
        String field = "TIME,kind,state,out01,out02,out03,out04,out05,out06,out07,out08,out09";
//			将日期间隔置为null
        Map<String, Object> dateFilter = new LinkedHashMap<>();
        try {
            dateFilter = DateFilter.dateFilter(dateEnd,dateFrist);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        dateFilter.put("interval", null);
        System.err.println(dateFilter);
        List<Object> dailyData = factoryService.getHistoryDatasByDate(dateFilter, out_day_tableName, field);//存放dailyData的json数组
//          表单的第一列数据
        String team1=paraAnalysisData.get("team_01").toString().substring(0,paraAnalysisData.get("team_01").toString().lastIndexOf(":"));
        String team2=paraAnalysisData.get("team_02").toString().substring(0,paraAnalysisData.get("team_02").toString().lastIndexOf(":"));
        String team3=paraAnalysisData.get("team_03").toString().substring(0,paraAnalysisData.get("team_03").toString().lastIndexOf(":"));
        log.info("team1:"+team1+",team2:"+team2+",team3:"+team3);

        for (int i = 0; i < dailyData.size(); i++) {
            String groupId = null;
            switch (i){
                case 0: groupId = "合计";break;
                case 1: groupId = "班组"+i+"("+team3+"-"+team1+")";break;
                case 2: groupId = "班组"+i+"("+team1+"-"+team2+")";break;
                case 3: groupId = "班组"+i+"("+team2+"-"+team3+")";break;
            }
            ((Map<String,Object>)dailyData.get(i)).put("groupId",groupId);
        }
        return JSONArray.fromObject(dailyData);
    }

    /**
     *  获取画昨日曲线的数据
     * @param analysis
     * @return
     */
    @PostMapping("/anal/dailyReport/line")
    @ResponseBody
    public JSONArray line(@RequestBody Analysis analysis){
        System.err.println(analysis);
        Integer factoryId = analysis.getFactoryId();
        String dateStart = analysis.getDateStart();
        String dateEnd = analysis.getDateEnd();
//        1 根据factoryId 获取para_ana1表的数据
        Map<String, Object> paraAnalysisData = factoryService.getParaAnalysisData(factoryId);
//        2 拼接字段名，和表名
        Map<String, Object> yDataPreHeadler = Tools.yesterdayDataPreHeadler(paraAnalysisData);
        String tableName = (String) yDataPreHeadler.get("tableName");
        String KFFields = (String) yDataPreHeadler.get("KFFields");

//		  3 根据日期调整查询表名：如，从dateStart:2017-11-15 15:00:00  中取出2017-11传入sql，用来动态选择指定月份某一表（如 KF0001_201711）
        System.err.println("dateStart:"+dateStart);
        String year = dateStart.substring(0, dateStart.indexOf("-"));
        String month = dateStart.substring(dateStart.indexOf("-")+1, dateStart.lastIndexOf("-"));
        if(month.length()==1) {
            month = "0"+month;
        }
        log.info("1、预查询表的年月份："+year+","+month);
        Calendar now = Calendar.getInstance();//实现分月查询
        int nowyear = now.get(Calendar.YEAR);
        int nowmonth = now.get(Calendar.MONTH)+1;
        log.info("2、当下年月："+nowyear+","+nowmonth);
        if(Integer.valueOf(year)!=nowyear || Integer.valueOf(month)!=nowmonth) {
            tableName = tableName + "_"+year+month;
        }
        log.info("3、最终要查询的表："+tableName);
//        4 查询时间调整
        Map<String, Object> dateFilter2 = new LinkedHashMap<>();
        try {
            dateFilter2 = DateFilter.dateFilter(dateStart,dateEnd);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        dateFilter2.put("interval", 72);
//        5 查询昨日历史数据
        List<Object> yDataList = new ArrayList<>();
        try {
            yDataList = factoryService.getHistoryDatasByDate(dateFilter2, tableName,KFFields);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        6 将lineNum传入页面，便于echarts动态创建曲线条数，lineNumMap插入到yDataList最后
        Map<String, Object> lineNumMap = new HashMap<>();
        lineNumMap.put("lineNum", paraAnalysisData.get("line_num"));
        String[] split = KFFields.split(",");
        for (int i = 0; i < split.length; i++) {
            lineNumMap.put("para" + i, split[i]);
        }
        yDataList.add(lineNumMap);

        return JSONArray.fromObject(yDataList);
    }

    /**
     * 每日数据分析
     * 1、表前面的日期：日报表(日期：2017-12-7)
     * 2、表格
     * 3、每日曲线（凌晨-凌晨）
     * 4、班组下显示时间
     * @param analysis
     * @return
     */
    @PostMapping("/dailyReport")
    public @ResponseBody JSONObject dailyReportPost(@RequestBody Analysis analysis, HttpServletResponse response) {
        System.out.println("analysis:"+analysis);
        // 0 初始化数据
        Integer factoryId = analysis.getFactoryId();
        String dateFrist = analysis.getDateFrist();
        String dateStart = analysis.getDateStart();
        String dateEnd = analysis.getDateEnd();
        System.err.println("dateStart:"+dateStart);
        System.err.println("dateEnd:"+dateEnd);
        System.err.println("dateFrist:"+dateFrist);
        // 1 根据factoryId 获取para_ana1表的数据
        Map<String, Object> paraAnalysisData = factoryService.getParaAnalysisData(factoryId);
//        System.err.println("paraAnalysisData:"+paraAnalysisData);
        // 2 根据班组编号查询数据，并传入动态表名
        String out_day_tableName = String.valueOf(paraAnalysisData.get("out_table_day"));
        String field = "TIME,kind,state,out01,out02,out03,out04,out05,out06,out07,out08,out09";
//			将日期间隔置为null
        Map<String, Object> dateFilter = new LinkedHashMap<>();
        try {
            dateFilter = DateFilter.dateFilter(dateEnd,dateFrist);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        dateFilter.put("interval", null);
//        System.err.println(dateFilter);
        List<Object> dailyData = factoryService.getHistoryDatasByDate(dateFilter, out_day_tableName, field);//存放dailyData的json数组
//        System.out.println("dailyData:"+dailyData);
        // 3 获取画昨日曲线的数据
        Map<String, Object> yDataPreHeadler = Tools.yesterdayDataPreHeadler(paraAnalysisData);
        String tableName = (String) yDataPreHeadler.get("tableName");
        String KFFields = (String) yDataPreHeadler.get("KFFields");

//		从dateStart:2017-11-15 15:00:00  中取出2017-11传入sql，用来动态选择指定月份某一表（如 KF0001_201711）
        System.err.println("dateStart:"+dateStart);
        String year = dateStart.substring(0, dateStart.indexOf("-"));
        String month = dateStart.substring(dateStart.indexOf("-")+1, dateStart.lastIndexOf("-"));
        if(month.length()==1) {
            month = "0"+month;
        }
        System.out.println(year+","+month);
        Calendar now = Calendar.getInstance();//实现分月查询
        int nowyear = now.get(Calendar.YEAR);
        int nowmonth = now.get(Calendar.MONTH)+1;
        System.out.println("当下年月："+nowyear+","+nowmonth);
        if(Integer.valueOf(year)!=nowyear || Integer.valueOf(month)!=nowmonth) {
            tableName = tableName + "_"+year+month;
        }
        System.out.println("查询的表："+tableName);

        List<Object> yDataList = new ArrayList<>();
        Map<String, Object> dateFilter2 = new LinkedHashMap<>();
        try {
            dateFilter2 = DateFilter.dateFilter(dateStart,dateEnd);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        dateFilter2.put("interval", 72);
        System.err.println(dateFilter2);
        try {
            yDataList = factoryService.getHistoryDatasByDate(dateFilter2, tableName,
                    KFFields);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        System.err.println("yDataList:"+yDataList);
        Map<String, Object> lineNumMap = new HashMap<>();// 将lineNum传入页面，便于动态创建曲线条数
        lineNumMap.put("lineNum", paraAnalysisData.get("line_num"));
        String[] split = KFFields.split(",");
        for (int i = 0; i < split.length; i++) {
            lineNumMap.put("para" + i, split[i]);
        }

        yDataList.add(lineNumMap);
//        System.out.println("yDataList:"+yDataList);
        // 转json传给页面
//		System.out.println(((Map<String, Object>)dailyData.get(1)).get("TIME").getClass());//测试从数据库得到得time类型：class java.sql.Timestamp
//		System.out.println(paraAnalysisData.get("team_01").getClass());//测试输出班组时间格式:class java.sql.Time
        Map<String, Object> result = new HashMap<>();
        String team1 = paraAnalysisData.get("team_01").toString();
        String team2 = paraAnalysisData.get("team_02").toString();
        String team3 = paraAnalysisData.get("team_03").toString();
        result.put("team_01", team1.substring(0,team1.length()-3));//17:00:00->17:00
        result.put("team_02", team2.substring(0,team2.length()-3));
        result.put("team_03", team3.substring(0,team3.length()-3));
        result.put("team_num", paraAnalysisData.get("team_num"));
        result.put("dailyData", dailyData);
        result.put("yDataList", yDataList);

        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerJsonValueProcessor(java.sql.Timestamp.class, new DateJsonValueProcessor("dd日HH:mm:ss"));
        JSONObject jsonObject = JSONObject.fromObject(result,jsonConfig);
//        System.out.println(jsonObject);

        return jsonObject;
    }


    @PostMapping("/anal/OperateCondition/board")
    public @ResponseBody Map<String, Object> dashBoard(@RequestBody Analysis analysis) throws ParseException {
//      1 获取表名
        String boardTableName = factoryService.getDashBoardTableName(analysis.getFactoryId());
        String KFFields = "TIME,out01";
        log.info("boardTableName:"+boardTableName+",KFFields:"+KFFields);
//      3 获取最新数据
        Map<String, Object> data = factoryService.getData(boardTableName, KFFields);
        DateFormat df= new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        for (Map.Entry<String,Object> map :data.entrySet()) {
            String key = map.getKey();
            switch (key){
                case "TIME":
                    data.put(key,df.format(map.getValue()));break;
                case "out01":
                    Double value = Double.valueOf(String.valueOf(map.getValue()));
                    if (value>=150.0){
                        data.put(key,150);break;
                    }
                    if (value<=0.0){
                        data.put(key,0);break;
                    }
            }
        }
        for (Map.Entry<String,Object> map :data.entrySet()) {
            log.info("key:"+map.getKey()+",value:"+map.getValue());
        }
        return  data;
    }

    /**
     * 设置所属行业
     * @return
     */
    @GetMapping("/setIndustry")
    public @ResponseBody String setIndustry(){
        String s = Template.setIndustry();
        System.err.println(s);
        return s;
    }


    @GetMapping("/getIndustry")
    public @ResponseBody String getIndustry(){
        String s = Template.getIndustry();
        System.err.println(s);
        return s;
    }

    @GetMapping("/api_add_template")
    public @ResponseBody String api_add_template(){
        String s = Template.api_add_template();
        System.err.println(s);
        return s;
    }

    @GetMapping("/get_all_private_template")
    public @ResponseBody String get_all_private_template(){
        String s = Template.get_all_private_template();
        System.err.println(s);
        return s;
    }

    @GetMapping("/send")
    public @ResponseBody String send(){
        String s = Template.send("osAgr1Eoe3jZu74qEve0b1_d6e7Y","factoryName","alarmTime","fucking");
        System.err.println(s);
        return s;
    }
}
