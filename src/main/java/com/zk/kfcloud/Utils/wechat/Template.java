package com.zk.kfcloud.Utils.wechat;

import com.google.gson.JsonObject;
import com.zk.kfcloud.Utils.RequestMethod;
import net.sf.json.JSONObject;

public class Template {

    public static final String SET_INDUSTRY_TEMPLATEURL = "https://api.weixin.qq.com/cgi-bin/template/api_set_industry?access_token=ACCESS_TOKEN";
    public static final String GET_INDUSTRY_TEMPLATEURL = "https://api.weixin.qq.com/cgi-bin/template/get_industry?access_token=ACCESS_TOKEN";
    public static final String API_ADD_TEMPLATEURL = "https://api.weixin.qq.com/cgi-bin/template/api_add_template?access_token=ACCESS_TOKEN";
    public static final String GET_ALL_PRIVATE_TEMPLATEURL= "https://api.weixin.qq.com/cgi-bin/template/get_all_private_template?access_token=ACCESS_TOKEN";
    public static final String DEL_PRIVATE_TEMPLATEURL= "https://api.weixin.qq.com/cgi-bin/template/del_private_template?access_token=ACCESS_TOKEN";
    public static final String SEND= "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";

    /**
     * 设置所属行业
     * @return
     */
    public static String setIndustry(){
        JSONObject postData = new JSONObject();
        postData.put("industry_id1","2");
        postData.put("industry_id2","41");
        String realTemplateUrl = SET_INDUSTRY_TEMPLATEURL.replace("ACCESS_TOKEN",MaterialManage.getAccessToken().getAccess_token());
        return RequestMethod.doPost(realTemplateUrl,postData.toString());
    }

    /**
     * 获取设置的行业信息
     * @return
     */
    public static String getIndustry(){
        String realTemplateUrl = GET_INDUSTRY_TEMPLATEURL.replace("ACCESS_TOKEN",MaterialManage.getAccessToken().getAccess_token());
        return RequestMethod.doGet(realTemplateUrl);
    }

    /**
     * 获得模板ID
     * @return
     */
    public static String api_add_template(){
        String realTemplateUrl = API_ADD_TEMPLATEURL.replace("ACCESS_TOKEN",MaterialManage.getAccessToken().getAccess_token());
        JSONObject postData = new JSONObject();
        postData.put("template_id_short","TM00204");
        return RequestMethod.doPost(realTemplateUrl,postData.toString());
    }

    /**
     * 获取模板列表
     * @return
     */
    public static String get_all_private_template(){
        String realTemplateUrl = GET_ALL_PRIVATE_TEMPLATEURL.replace("ACCESS_TOKEN",MaterialManage.getAccessToken().getAccess_token());
        return RequestMethod.doGet(realTemplateUrl);
    }

    /**
     * 删除模板
     * @return
     */
    public static String del_private_template(String template_id){
        String realTemplateUrl = DEL_PRIVATE_TEMPLATEURL.replace("ACCESS_TOKEN",MaterialManage.getAccessToken().getAccess_token());
        JSONObject postData = new JSONObject();
        postData.put("template_id",template_id);
        return RequestMethod.doPost(realTemplateUrl,postData.toString());
    }

    /**
     * 发送模板消息
     * @return
     */
    public static String send(String openId,String factoryName,String alarmTime,String content){
        String realTemplateUrl = SEND.replace("ACCESS_TOKEN",MaterialManage.getAccessToken().getAccess_token());
        JSONObject postData = new JSONObject();
        postData.put("touser",openId);
        postData.put("template_id","TRVS9WLzPTPCH82zW_135kppYDj9OyEUMeOWlDAixCc");

        JSONObject data = new JSONObject();

        JSONObject first = new JSONObject();
        first.put("value",factoryName+"报警信息!");
        first.put("color","#173177");
        data.put("first",first);

        JSONObject time = new JSONObject();
        time.put("value",alarmTime);
        time.put("color","#173177");
        data.put("time",time);

        JSONObject performance = new JSONObject();
        performance.put("value",content);
        performance.put("color","#173177");
        data.put("performance",performance);

        JSONObject remark = new JSONObject();
        remark.put("value",null);
        remark.put("color","#173177");
        data.put("remark","请及时处理!");

        postData.put("data",data);//{"errcode":45027,"errmsg":"template conflict with industry hint: [73LIoa0753vr35]"}

        System.err.println(postData);
        return RequestMethod.doPost(realTemplateUrl,postData.toString());
    }
}
