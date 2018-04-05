package com.zk.kfcloud.Utils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Tools {
    /**
     * 解决get乱码（反编码）
     *
     * @param str
     * @return String
     */
    public static String encodeStr(String str) {
        try {
            return new String(str.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 检测字符串是否不为空(null,"","null")
     *
     * @param s
     * @return 不为空则返回true，否则返回false
     */
    public static boolean notEmpty(String s) {
        return s != null && !"".equals(s) && !"null".equals(s);
    }

    /**
     * 检测字符串是否为空(null,"","null")
     *
     * @param s
     * @return 为空则返回true，不否则返回false
     */
    public static boolean isEmpty(String s) {
        return s == null || "".equals(s) || "null".equals(s);
    }

    /**
     * 字符串转换为字符串数组
     *
     * @param str        字符串
     * @param splitRegex 分隔符
     * @return
     */
    public static String[] str2StrArray(String str, String splitRegex) {
        if (isEmpty(str)) {
            return null;
        }
        return str.split(splitRegex);
    }

    /**
     * 用默认的分隔符(,)将字符串转换为字符串数组
     *
     * @param str 字符串
     * @return
     */
    public static String[] str2StrArray(String str) {
        return str2StrArray(str, ",\\s*");
    }

    /**
     * 按照yyyy-MM-dd HH:mm:ss的格式，日期转字符串
     *
     * @param date
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String date2Str(Date date) {
        return date2Str(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 按照yyyy-MM-dd HH:mm:ss的格式，字符串转日期
     *
     * @param date
     * @return
     */
    public static Date str2Date(String date) {
        if (notEmpty(date)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                return sdf.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return new Date();
        } else {
            return null;
        }
    }


    /**
     * 按照参数format的格式，日期转字符串
     *
     * @param date
     * @param format
     * @return
     */
    public static String date2Str(Date date, String format) {
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(date);
        } else {
            return "";
        }
    }

    //---------------------------------------------------------------------


    /**
     * 写txt里的单行内容
     *
     * @param fileP 文件路径
     * @param content  写入的内容
     */
//    public static void writeFile(String fileP, String content) {
//        String filePath = String.valueOf(Thread.currentThread().getContextClassLoader().getResource("")) + "../../";    //项目路径
//        filePath = (filePath.trim() + fileP.trim()).substring(6).trim();
//        if (filePath.indexOf(":") != jquery) {
//            filePath = File.separator + filePath;
//        }
//        try {
//            OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(filePath), "utf-8");
//            BufferedWriter writer = new BufferedWriter(write);
//            writer.write(content);
//            writer.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


    /**
     * 读取txt里的单行内容
     */
//    public static String readTxtFile(String fileP) {
//        try {
//
//            String filePath = String.valueOf(Thread.currentThread().getContextClassLoader().getResource("")) + "../../";    //项目路径
//            filePath = filePath.replaceAll("file:/", "");
//            filePath = filePath.replaceAll("%20", " ");
//            filePath = filePath.trim() + fileP.trim();
//            if (filePath.indexOf(":") != jquery) {
//                filePath = File.separator + filePath;
//            }
//            String encoding = "utf-8";
//            File file = new File(filePath);
//            if (file.isFile() && file.exists()) {        // 判断文件是否存在
//                InputStreamReader read = new InputStreamReader(
//                        new FileInputStream(file), encoding);    // 考虑到编码格式
//                BufferedReader bufferedReader = new BufferedReader(read);
//                String lineTxt = null;
//                while ((lineTxt = bufferedReader.readLine()) != null) {
//                    bufferedReader.close();
//                    return lineTxt;
//                }
//                read.close();
//                bufferedReader.close();
//            } else {
//                System.out.println("找不到指定的文件,查看此路径是否正确:" + filePath);
//            }
//        } catch (Exception e) {
//            System.out.println("读取文件内容出错");
//        }
//        return "";
//    }

    public static String md5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte[] b = md.digest();
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                int i = b[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            str = buf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 输出流，文件输出
     * @param response
     * @param o
     */
    public static void write(HttpServletResponse response, Object o) {
        response.setContentType("text/html;charset=utf-8");
        try {
            PrintWriter out = response.getWriter();
            out.println(o.toString());
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


//	public static void main(String[] args) {
//		String password="123456";
//		System.out.println("Md5加密："+md5(password));
//	}

}
