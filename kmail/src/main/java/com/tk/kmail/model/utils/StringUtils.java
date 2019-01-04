package com.tk.kmail.model.utils;


import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by asus on 2017/6/20.
 */

public class StringUtils {


    /**
     * 判断文本是否是json格式
     *
     * @return
     */
    public static boolean isJson(String jsonString) {
        if (TextUtils.isEmpty(jsonString))
            return false;
        try {
            new JSONObject(jsonString);
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            new JSONArray(jsonString);
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 取两个文本之间的文本值
     *
     * @param text
     * @param left
     * @param right
     * @return
     */
    public static String getSubString(String text, String left, String right) {
        String result = "";
        int zLen;
        if (left == null || left.length() == 0) {
            zLen = 0;
        } else {
            zLen = text.indexOf(left);
            if (zLen > -1) {
                zLen += left.length();
            } else {
                zLen = 0;
            }
        }
        int yLen = text.indexOf(right, zLen);
        if (yLen < 0 || right == null || right.length() == 0) {
            yLen = text.length();
        }
        result = text.substring(zLen, yLen);
        return result;
    }

    public static boolean isEmpty(String... sts) {
        for (String x : sts) {
            if (x == null || x.length() < 1)
                return true;
        }
        return false;
    }

    /**
     * 数字转大写
     *
     * @param numb
     * @return
     */
    public static String formatNumber(int numb) {
        String[] units = {"", "十", "百", "千", "万", "十万", "百万", "千万", "亿",
                "十亿", "百亿", "千亿", "万亿"};
        char[] numArray = {'零', '一', '二', '三', '四', '五', '六', '七', '八', '九'};
        boolean flag = false;
        char[] charArray = String.valueOf(numb).toCharArray();
        StringBuilder sub = new StringBuilder();
        for (int i = 0; i < charArray.length; i++) {
            int num = Integer.valueOf(charArray[i] + "");
            int byts = (charArray.length - 1) - i;
            if (num == 0) {
                if (flag == false) {
                    flag = true;
                }
            } else {
                if (flag) {
                    flag = false;
                    sub.append(numArray[num]);
                } else {
                    if (num == 1 && byts == num) {
                        sub.append(units[byts]);
                    } else {
                        sub.append(numArray[num]).append(units[byts]);
                    }

                }
            }
        }
        return sub.toString();
    }


}
