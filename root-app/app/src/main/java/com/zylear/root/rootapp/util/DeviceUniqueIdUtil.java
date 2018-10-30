package com.zylear.root.rootapp.util;

import android.app.Activity;
import android.os.Build;
import android.widget.Toast;

import com.zylear.root.rootapp.RegisterActivity;
import com.zylear.root.rootapp.bean.AppCache;
import com.zylear.root.rootapp.handler.ToastHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DeviceUniqueIdUtil {


    public static String getUniquePsuedoID() {
        String serial = "serial";  //默认

        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +

                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +

                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +

                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +

                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +

                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +

                Build.USER.length() % 10; //13 位


        try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();
            //API>=9 使用serial号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            //serial需要一个初始化
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        }
        //使用硬件信息拼凑出来的15位号码
    }


    public static String getDeviceId(Activity activity) {
        String deviceId = AppCache.deviceId;
        if (StringUtil.isEmpty(deviceId)) {
            deviceId = SharedPreferencesUtil.read(activity, "deviceId");
            if (StringUtil.isEmpty(deviceId)) {
                deviceId = DeviceUniqueIdUtil.getUniquePsuedoID();
                if (!StringUtil.isEmpty(deviceId)) {
                    Map<String, String> map = new HashMap<>();
                    map.put("deviceId", deviceId);
                    SharedPreferencesUtil.write(activity, map);
                    AppCache.deviceId = deviceId;
                }
            } else {
                AppCache.deviceId = deviceId;
            }
        }
        return deviceId;
    }

}
