package com.zk.kfcloud.Controller;

import com.zk.kfcloud.Entity.web.Factory;
import com.zk.kfcloud.Service.FactoryService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class FactoryController {

    @Autowired
    private FactoryService factoryService;

    @GetMapping("/subMenu/{pagename}/{factoryId}")
    public String sendPage(@PathVariable("pagename") String pagename, @PathVariable("factoryId") String factoryId, Model model){
        model.addAttribute("factoryId",factoryId);
        //        去掉后缀'.html'
        return pagename.substring(0, pagename.indexOf("."));
    }

    @GetMapping("/sys")
    public String sys(){
        return "current";
    }

    /**
     * 返回某工厂current页面拥有的空分系统数据
     * @param id
     * @return
     */

    @GetMapping("/current/{factoryId}")
    @ResponseBody
    public List<Factory> curSystem(@PathVariable("factoryId") Integer id){

        return factoryService.listAllInfoByFactoryId(id);
    }

    @GetMapping("/current/{modelName}/{modelId}/{systemName}")
    @ResponseBody
    public JSONObject curSystem(@PathVariable("modelName") String modelname, @PathVariable("modelId") Integer id, @PathVariable("systemName") String systemname){
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
}
