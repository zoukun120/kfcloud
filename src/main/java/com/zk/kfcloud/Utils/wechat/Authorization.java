//package com.zk.kfcloud.Utils.wechat;
//
//import net.sf.json.JSONObject;
//
//import java.io.UnsupportedEncodingException;
//import java.net.URLEncoder;
//
///**
// * 授权
// * @author feixiang
// *
// */
//public class Authorization {
//
//	public static final String CODE_URL ="https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
//	public static final String TOKEN_URL ="https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
//	public static final String USER_Url ="https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
//	public static final String REDIRECT_URI = MaterialManage.DOMAIN+"/WeChat/callback";
//
//	public static String getCodeUrl() {
//		String codeUrl = null;
//		try {
//			codeUrl =CODE_URL.replace("APPID", MaterialManage.APPID).replace("REDIRECT_URI", URLEncoder.encode(REDIRECT_URI, "UTF-8")).replace("SCOPE", "snsapi_userinfo");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		return codeUrl;
//	}
//
//	public static JSONObject getTokenAndOpenId(String code) {
//		String tokenUrl = null;
//		if (code != null) {
//			tokenUrl ="https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code"
//					.replace("APPID", MaterialManage.APPID).replace("SECRET", MaterialManage.APPSECRET).replace("CODE", code);
//		}
//		return RequestMethod.doGet(tokenUrl);
//	}
//
//	public static JSONObject getUserInfo(String access_token, String openid) {
//		String userInfoUrl ="https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN"
//				.replace("ACCESS_TOKEN", access_token).replace("OPENID", openid);
//		return RequestMethod.doGet(userInfoUrl);
//	}
//}
