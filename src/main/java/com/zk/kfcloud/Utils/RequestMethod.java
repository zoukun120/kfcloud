package com.zk.kfcloud.Utils;


import net.sf.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class RequestMethod {
    public static String doGet(String url) {
        HttpGet httpGet = new HttpGet(url);
        return RequestMethod.Send(httpGet);
    }

    public static String doPost(String url, String entityString) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new StringEntity(entityString, "UTF-8"));//
        return RequestMethod.Send(httpPost);
    }

    public static String Send(HttpUriRequest httpUriRequest) {
        JSONObject obj = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpResponse reponse = httpClient.execute(httpUriRequest);
            return EntityUtils.toString(reponse.getEntity(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
