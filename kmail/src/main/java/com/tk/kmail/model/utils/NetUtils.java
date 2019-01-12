package com.tk.kmail.model.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.tk.kmail.App;

/**
 * Created by TangKai on 2017/11/9.
 */

public class NetUtils {
    public static final int NETCODE_NO_NET = 0;
    public static final int NETCODE_WIFI = 1;
    public static final int NETCODE_MOBILE = 2;

    /**
     * 判断网络连接
     */
    public static boolean isNetworkAvailable() {
//        boolean flag = false;
//        ConnectivityManager conMan = (ConnectivityManager) MyApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = conMan.getActiveNetworkInfo();
//        if (networkInfo != null) {
//            flag = true;
//        }
        return getNetWorkStatus() != NETCODE_NO_NET;
    }

    /**
     * 获取当前的网络状态 ：没有网络-0：WIFI网络1：4G网络-4：3G网络-3：2G网络-2
     * 自定义
     *
     * @return
     */
    public static int getNetWorkStatus() {
        Context context = App.context;
        //结果返回值
        int netType = NETCODE_NO_NET;
        //获取手机所有连接管理对象
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //获取NetworkInfo对象
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        //NetworkInfo对象为空 则代表没有网络
        if (networkInfo == null) {
            return netType;
        }
        //否则 NetworkInfo对象不为空 则获取该networkInfo的类型
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_WIFI) {
            //WIFI
            netType = NETCODE_WIFI;
//            netType=1;
        } else if (nType == ConnectivityManager.TYPE_MOBILE) {
            int nSubType = networkInfo.getSubtype();
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            //3G   联通的3G为UMTS或HSDPA 电信的3G为EVDO
            if (nSubType == TelephonyManager.NETWORK_TYPE_LTE
                    && !telephonyManager.isNetworkRoaming()) {
                netType = NETCODE_MOBILE;
//                netType = 4;
            } else if (nSubType == TelephonyManager.NETWORK_TYPE_UMTS
                    || nSubType == TelephonyManager.NETWORK_TYPE_HSDPA
                    || nSubType == TelephonyManager.NETWORK_TYPE_EVDO_0
                    && !telephonyManager.isNetworkRoaming()) {
                netType = NETCODE_MOBILE;
//                netType = 3;
                //2G 移动和联通的2G为GPRS或EGDE，电信的2G为CDMA
            } else if (nSubType == TelephonyManager.NETWORK_TYPE_GPRS
                    || nSubType == TelephonyManager.NETWORK_TYPE_EDGE
                    || nSubType == TelephonyManager.NETWORK_TYPE_CDMA
                    && !telephonyManager.isNetworkRoaming()) {
                netType = NETCODE_MOBILE;
//                netType = 2;
            } else {
                netType = NETCODE_MOBILE;
//                netType = 2;
            }
        }
        return netType;
    }

}
