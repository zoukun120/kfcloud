package com.zk.kfcloud.Utils.wechat;


import com.zk.kfcloud.Entity.wechat.AccessToken;
import com.zk.kfcloud.Utils.RequestMethod;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class MaterialManage {
    public static long time = 0;
    public static AccessToken token = new AccessToken();
    //    正式上线
    public static final String DOMAIN = "http://wx.kongfenyun.com";
    public static final String APPID = "wx7aa9af01712b950a";
    public static final String APPSECRET = "ce51a61eb1cda2c839031df91c18fea1";
    //    本地调试
//    public static final String DOMAIN = "http://9te3hg.natappfree.cc";
//    public static final String APPID = "wx659f521d004e0415";
//    public static final String APPSECRET = "c5826cbeada6a49452000ae174ad31e6";

    public static final String AccessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + APPID + "&secret=" + APPSECRET;
    public static final String UploadUrl = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
    /**
     * get AccessToken
     *
     * @return
     */
    public static AccessToken getAccessToken() throws ParseException {
//        AccessToken token = new AccessToken();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String datetime = dateFormat.format(new Date());
        Long now = dateFormat.parse(datetime).getTime();
        log.info("time:" + time);
        log.info("nowtime:" + now);
        if((now-time)>=(60*1000*60)){
            time = now;
            log.info(" 超过一小时重新获取AccessToken，当前时间为"+datetime);
            JSONObject jsonObj = JSONObject.fromObject(RequestMethod.doGet(AccessTokenUrl));
//            System.err.println(jsonObj);
            if (jsonObj != null) {
                token.setAccess_token(jsonObj.getString("access_token"));
                token.setExpires_in(jsonObj.getInt("expires_in"));
            } };
        log.info("AccessToken:" + token);
        return token;
    }

    /**
     * upload media material
     *
     * @param filePath
     * @param accessToken
     * @param type
     * @return
     * @throws IOException
     */
    public static String upload(String filePath, String accessToken, String type) throws IOException {
        File file = new File(filePath);
        if (!file.isFile() || !file.exists()) {
            throw new IOException("文件不存在");
        }
        String url = UploadUrl.replace("ACCESS_TOKEN", accessToken).replaceAll("TYPE", type);
        URL urlObj = new URL(url);

        HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);

        //设置请求头
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("Charset", "utf-8");

        //设置边界
        String BOUNDARY = "----------" + System.currentTimeMillis();
        connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + BOUNDARY);

        StringBuilder sb = new StringBuilder();
        sb.append("--");
        sb.append(BOUNDARY);
        sb.append("\r\n");
        sb.append("Content-Disposition;form-data;name=\"flie\";filename=\"" + file.getName() + "\"\r\n");
        sb.append("Content-Type:application/octet-stream\r\n\r\n");
        byte[] head = sb.toString().getBytes("utf-8");

        //获取输出流
        OutputStream out = new DataOutputStream(connection.getOutputStream());
        out.write(head);

        //文件正文部分，把文件以流的方式push到url中
        DataInputStream in = new DataInputStream(new FileInputStream(file));
        int bytes = 0;
        byte[] bufferOut = new byte[1024];
        while ((bytes = in.read(bufferOut)) != -1) {
            out.write(bufferOut, 0, bytes);
        }
        in.close();

        //结尾部分
        byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");
        out.write(foot);
        out.close();

        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = null;
        String result = null;
        try {
            //读取url响应结果
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            if (result == null) {
                result = buffer.toString();
            }
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        JSONObject jsonObject = new JSONObject().getJSONObject(result);
        System.out.println(jsonObject);
        String typeName = "media_id";//上传image，video,voice后返回的media_id名称不同
        if (!"image".equals(type)) {
            typeName = type + "_media_id";
        }
        String mediaId = jsonObject.getString(typeName);
        System.out.println("typeName=" + typeName);
        return mediaId;
    }
}
