package com.zk.kfcloud.Utils.wechat;

import com.thoughtworks.xstream.XStream;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MsgUtil {

    public static Map<String, String> xmlToMap(HttpServletRequest request) throws  Exception {
        Map<String, String> map = new HashMap<String,String>();
        InputStream inputStream = request.getInputStream();
        SAXReader reader = new SAXReader();
        Document read = reader.read(inputStream);
        Element rootElement = read.getRootElement();
        List<Element> elements = rootElement.elements();
        for (Element element : elements) {
            map.put(element.getName(), element.getText());
        }
        inputStream.close();
        return map;
    }

    public static String mapToXml(textMessage text){
//        stream.toXML(text)的默认全类名<zk.entity.textMessage>、stream.alias首尾全类名改成<xml>
        XStream stream = new XStream();
        stream.alias("xml",text.getClass());
        return stream.toXML(text);
    }

}
