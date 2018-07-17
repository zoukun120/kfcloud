package com.zk.kfcloud.Utils.wechat;


import com.google.gson.Gson;
import com.zk.kfcloud.Entity.web.UserInfo;
import com.zk.kfcloud.Utils.JSONUtil;
import com.zk.kfcloud.Utils.RequestMethod;
import net.sf.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public static final String OpenID_URL= "https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&next_openid=NEXT_OPENID";
    public static final String Alluserinfo_URL= "https://api.weixin.qq.com/cgi-bin/user/info/batchget?access_token=ACCESS_TOKEN";

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
    /*
            access_token（微信公众号唯一接口）获取关注该公众号的所有OpenID,并批量获取用户信息
         */
//    public static String OpenID_URL( String access_token) {
//        String OpenIDInfoUrl = OpenID_URL.replace("ACCESS_TOKEN", access_token).replace("NEXT_OPENID","");
//        return RequestMethod.doGet(OpenIDInfoUrl);
//    }

    /**
         * 获取公众号关注的用户openid
         */
    public static List<String>  getallopenId (String access_token, String nextOpenid ) throws ParseException {
        List<String> result = null;
        String OpenIDInfoUrl = OpenID_URL.replace("ACCESS_TOKEN",access_token).replace("NEXT_OPENID",nextOpenid);
        String OpenIDInfo =RequestMethod.doGet(OpenIDInfoUrl);
        JSONObject OpenIDinfo = JSONObject.fromObject(OpenIDInfo);
        JSONObject allOpenID= OpenIDinfo.getJSONObject("data");
        result = (List<String>) allOpenID.get("openid");
        return result;
    }
    /**
         * 通过openid批量获取用户信息
         * @param access_token
         * @param postData
         * @return
     */
    public static String getUserInfobyOpenIds(String access_token, String postData) {
        String AlluserInfo_URL = Alluserinfo_URL.replace("ACCESS_TOKEN",access_token);
        return RequestMethod.doPost(AlluserInfo_URL,postData);
    }
    /**
           * 解析返回用户信息数据
           * @param userInfoJSON
           * @return
           */
    public static List<UserInfo> parseUserInfo(String userInfoJSON)
     {          List user_info_list = new ArrayList<UserInfo>();
                Map tMapData = JSONUtil.toMap(userInfoJSON);
                List<Map> tUserMaps = (List<Map>) tMapData.get("user_info_list");
               for (int i = 0; i < tUserMaps.size(); i++)
                    {   UserInfo tUserInfo = new UserInfo();
                        tUserInfo.setSubscribe(Integer.valueOf(new Double((double) tUserMaps.get(i).get("subscribe")).intValue()));
//                        tUserInfo.setSubscribe((Integer) tUserMaps.get(i).get("subscribe"));
                        tUserInfo.setSubscribe(Integer.valueOf(new Double((double) tUserMaps.get(i).get("sex")).intValue()));
//                        tUserInfo.setSex((Integer) tUserMaps.get(i).get("sex"));
                        tUserInfo.setOpenId((String) tUserMaps.get(i).get("openid"));
                        tUserInfo.setNickname((String) tUserMaps.get(i).get("nickname"));
                        tUserInfo.setLanguage((String) tUserMaps.get(i).get("language"));
                        tUserInfo.setCity((String) tUserMaps.get(i).get("city"));
                        tUserInfo.setProvince((String) tUserMaps.get(i).get("province"));
                        tUserInfo.setCountry((String) tUserMaps.get(i).get("country"));
                        tUserInfo.setHeadimgurl((String) tUserMaps.get(i).get("headimgurl"));
                        tUserInfo.setSubscribe(Integer.valueOf(new Double((double) tUserMaps.get(i).get("subscribe_time")).intValue()));
//                        tUserInfo.setSubscribetime((Integer) tUserMaps.get(i).get("subscribe_time"));
                        tUserInfo.setRemark((String) tUserMaps.get(i).get("remark"));
                        tUserInfo.setSubscribe(Integer.valueOf(new Double((double) tUserMaps.get(i).get("groupid")).intValue()));
//                        tUserInfo.setGroupid((Integer) tUserMaps.get(i).get("groupid"));
                        user_info_list.add(tUserInfo);
                     }
                return user_info_list;
             }

    /**
     * 过滤emoji 或者 其他非文字类型的字符
     * @param source
     * @return
     */
    public static String filterEmoji(String source)
    {
        int len = source.length();
        StringBuilder buf = new StringBuilder(len);
        for (int i = 0; i < len; i++)
        {
            char codePoint = source.charAt(i);
            if (isNotEmojiCharacter(codePoint))
            {
                buf.append(codePoint);
            } else{
                buf.append("*");
            }
        }
        return buf.toString();
    }

    private static boolean isNotEmojiCharacter(char codePoint)
    {
        return (codePoint == 0x0) ||
                (codePoint == 0x9) ||
                (codePoint == 0xA) ||
                (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }
}
