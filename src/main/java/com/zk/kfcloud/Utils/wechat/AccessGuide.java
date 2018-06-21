package com.zk.kfcloud.Utils.wechat;

import com.zk.kfcloud.Utils.Tools;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.util.Arrays;

@Slf4j
public class AccessGuide {

    public static final String token = "wechat";

    /**
     * 接受请求，并判断是否来自于WeChat服务器
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public static void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        if (checkSignature(signature, timestamp, nonce)) {
            Tools.write(response, echostr);
            log.info("Access to wechat server successfully");
        }
    }

    /**
     * Check Signature
     *
     * @param signature
     * @param timestamp
     * @param nonce
     * @return
     */
    public static boolean checkSignature(String signature, String timestamp, String nonce) {
        String[] strs = {token, timestamp, nonce};
        Arrays.sort(strs);

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < strs.length; i++) {
            sb.append(strs[i]);
        }
        return signature.equals(getSha1(sb.toString()));
    }

    /**
     * Encryption Algorithm
     *
     * @param str
     * @return
     */
    public static String getSha1(String str) {
        if ((str == null) || (str.length() == 0)) {
            return null;
        }
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] buf = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[(k++)] = hexDigits[(byte0 >>> 4 & 0xF)];
                buf[(k++)] = hexDigits[(byte0 & 0xF)];
            }
            return new String(buf);
        } catch (Exception e) {
        }
        return null;
    }
}
