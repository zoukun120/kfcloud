package com.zk.kfcloud.Utils.wechat;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

public class MsgManage extends HttpServlet {

    private final static String TEXT = "text";
    private final static String IMAGE = "image";
    private final static String VOICE = "voice";
    private final static String VIDEO = "video";
    private final static String MUSIC = "music";
    private final static String NEWS = "news";
    private final static String EVENT = "event";

    /**
     * 用户发送消息时，微信服务器会以post方式将信息发送到后台服务器
     * @param request
     * @return
     */
    public static String processRequest(HttpServletRequest request) {

        String responseContent = "初始化信息";
        String responseMessage = "success";//ֱ保证微信服务器5秒内能接受到回复

        try {
//          1、利用写好的工具类将微信服务器发来的xml格式消息解析到map里
            Map<String, String> map = MsgUtil.xmlToMap(request);


//          2、分类别返回消息给用户
            String msgType = map.get("MsgType");
            switch (msgType){
                case EVENT:
                    if ("subscribe".equals(map.get("Event"))) {
                        responseContent="很荣幸，您订阅公众号！\n点击菜单栏选择，进去空分云系统！";
                    }
                    else if ("unsubscribe".equals(map.get("Event"))) {
                        responseContent="很遗憾，你取消了本公众号！";
                    };
                    break;
                default:
                    responseContent="请点击菜单，进入空分云系统！";break;
            }

//          3、组装成xml格式的返回消息，发送给微信服务器
            textMessage text = new textMessage();
            text.setFromUserName(map.get("ToUserName"));
            text.setToUserName(map.get("FromUserName"));
            text.setCreateTime(new Date().getTime());
            text.setMsgType(TEXT);
            text.setContent(responseContent);

            responseMessage = MsgUtil.mapToXml(text);//利用写好的工具类将text类型消息拼装成微信服务器接受的xml格式
            System.out.println("回复给微信服务器的text消息：\n"+responseMessage);
        }
        catch (Exception  e) {
            e.printStackTrace();
        }
        return responseMessage;
    }
}
