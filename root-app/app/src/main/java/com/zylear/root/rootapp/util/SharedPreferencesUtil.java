package com.zylear.root.rootapp.util;

import android.app.Activity;
import android.content.SharedPreferences;

import java.util.Map;

public class SharedPreferencesUtil {

    private static final String CONFIG_KEY = "zy_config";

    public static void write(Activity activity, Map<String, String> params) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(CONFIG_KEY, 0);//创建一个实例
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            editor.putString(entry.getKey(), entry.getValue());
        }
        editor.commit();
    }


    public static String read(Activity activity, String key) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(CONFIG_KEY, 0);//创建一个实例
        return sharedPreferences.getString(key, "");
    }

}
