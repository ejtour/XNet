package com.fire.lib.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.telephony.TelephonyManager;

public class NetworkUtil {
    public static final String TAG = "NetworkUtil";

    private static final String CONNECT_TYPE_CTNET = "ctnet";

    private static final String CONNECT_TYPE_CTWAP = "ctwap";

    private static final String CONNECT_TYPE_CMNET = "cmnet";

    private static final String CONNECT_TYPE_CMWAP = "cmwap";

    private static final String CONNECT_TYPE_UNIWAP = "uniwap";

    private static final String CONNECT_TYPE_UNINET = "uninet";

    private static final String CONNECT_TYPE_UNI3GWAP = "3gwap";

    private static final String CONNECT_TYPE_UNI3GNET = "3gnet";

    private static final Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");


    /**
     * 判断APNTYPE
     *
     * @param context
     * @return
     */
    /**
     * @deprecated 4.0
     * doc:
     * Since the DB may contain corp passwords, we should secure it. Using the same permission as writing to the DB as the read is potentially as damaging as a write
     */
    public static String getApnType(Context context) {

        String apntype = "nomatch";
        Cursor c = context.getContentResolver().query(PREFERRED_APN_URI, null, null, null, null);
        if (c != null) {
            if (c.moveToFirst()) {
                String user = c.getString(c.getColumnIndex("user"));
                if (user != null && user.startsWith(CONNECT_TYPE_CTNET)) {
                    apntype = CONNECT_TYPE_CTNET;
                } else if (user != null && user.startsWith(CONNECT_TYPE_CTWAP)) {
                    apntype = CONNECT_TYPE_CTWAP;
                } else if (user != null && user.startsWith(CONNECT_TYPE_CMWAP)) {
                    apntype = CONNECT_TYPE_CMWAP;
                } else if (user != null && user.startsWith(CONNECT_TYPE_CMNET)) {
                    apntype = CONNECT_TYPE_CMNET;
                } else if (user != null && user.startsWith(CONNECT_TYPE_UNIWAP)) {
                    apntype = CONNECT_TYPE_UNIWAP;
                } else if (user != null && user.startsWith(CONNECT_TYPE_UNINET)) {
                    apntype = CONNECT_TYPE_UNINET;
                } else if (user != null && user.startsWith(CONNECT_TYPE_UNI3GWAP)) {
                    apntype = CONNECT_TYPE_UNI3GWAP;
                } else if (user != null && user.startsWith(CONNECT_TYPE_UNI3GNET)) {
                    apntype = CONNECT_TYPE_UNI3GNET;
                }
            }
            c.close();
            c = null;
        }

        return apntype;
    }



    /**
     * 获取可用的网络信息
     *
     * @param context
     * @return
     */
    private static NetworkInfo getActiveNetworkInfo(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 网络类型
     */
    public static final int NETWORK_CLASS_UNKNOWN = 0;

    public static final int NETWORK_CLASS_2_G = 1;

    public static final int NETWORK_CLASS_3_G = 2;

    public static final int NETWORK_CLASS_4_G = 3;

    public static final int NETWORK_CLASS_WIFI = 10;


    /**
     * 判断是否有网络可用
     *
     * @param context
     * @return
     */
    public static boolean isNetAvailable(Context context) {
        NetworkInfo networkInfo = getActiveNetworkInfo(context);
        if (networkInfo != null) {
            return networkInfo.isAvailable();
        } else {
            return false;
        }
    }



    /**
     * 获取在Mobile网络下的网络类型. 2G,3G,4G
     *
     * @param context
     * @return
     */
    public static int getNetworkClass(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null) {
                if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                    switch (networkInfo.getSubtype()) {
                        case TelephonyManager.NETWORK_TYPE_GPRS:
                        case TelephonyManager.NETWORK_TYPE_EDGE:
                        case TelephonyManager.NETWORK_TYPE_CDMA:
                        case TelephonyManager.NETWORK_TYPE_1xRTT:
                        case TelephonyManager.NETWORK_TYPE_IDEN:
                            return NETWORK_CLASS_2_G;
                        case TelephonyManager.NETWORK_TYPE_UMTS:
                        case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        case TelephonyManager.NETWORK_TYPE_EVDO_A:
                        case TelephonyManager.NETWORK_TYPE_HSDPA:
                        case TelephonyManager.NETWORK_TYPE_HSUPA:
                        case TelephonyManager.NETWORK_TYPE_HSPA:
                        case 12: // TelephonyManager.NETWORK_TYPE_EVDO_B:
                        case 14: // TelephonyManager.NETWORK_TYPE_EHRPD:
                        case 15: // TelephonyManager.NETWORK_TYPE_HSPAP:
                            return NETWORK_CLASS_3_G;
                        case 13: // TelephonyManager.NETWORK_TYPE_LTE:
                            return NETWORK_CLASS_4_G;
                        default:
                            return NETWORK_CLASS_UNKNOWN;
                    }
                } else if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    return NETWORK_CLASS_WIFI;
                }
            }
        }
        return NETWORK_CLASS_UNKNOWN;
    }

    public static String getNetworkClassStr(Context context) {
        int networkNum = NetworkUtil.getNetworkClass(context);
        String network;
        switch (networkNum) {
            case NETWORK_CLASS_2_G:
                network = "2G";
                break;
            case NETWORK_CLASS_3_G:
                network = "3G";
                break;
            case NETWORK_CLASS_4_G:
                network = "4G";
                break;
            case NETWORK_CLASS_WIFI:
                network = "WIFI";
                break;
            default:
                network = "UNKNOWN";
                break;
        }
        return network;
    }
}
