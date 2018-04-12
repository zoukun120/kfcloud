package com.zk.kfcloud.Utils.wechat;


import com.zk.kfcloud.Utils.RequestMethod;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 授权
 *
 * @author zoukun
 */
public class Authorization {

    public static final String CODE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
    public static final String TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    public static final String USER_Url = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
    public static final String REDIRECT_URI = MaterialManage.DOMAIN + "/redirect_uri";


    /*
        code作为换取access_token的票据，每次用户授权带上的code将不一样，code只能使用一次，5分钟未被使用自动过期。
     */
    public static String CodeUrl() throws UnsupportedEncodingException {
        return CODE_URL.replace("APPID", MaterialManage.APPID).replace("REDIRECT_URI", URLEncoder.encode(REDIRECT_URI, "UTF-8")).replace("SCOPE", "snsapi_userinfo");
    }

    /*
        用code作为换取access_token
     */
    public static String getTokenAndOpenId(String code) {
        String tokenUrl = null;
        if (code != null) {
            tokenUrl = TOKEN_URL.replace("APPID", MaterialManage.APPID).replace("SECRET", MaterialManage.APPSECRET).replace("CODE", code);
        }
        return RequestMethod.doGet(tokenUrl);
    }

    /**
     * 通过access_token和openid拉取用户信息
     *
     * @param access_token
     * @param openid
     * @return
     */
    public static String getUserInfo(String access_token, String openid) {
        String userInfoUrl = USER_Url.replace("ACCESS_TOKEN", access_token).replace("OPENID", openid);
        return RequestMethod.doGet(userInfoUrl);
    }
}
