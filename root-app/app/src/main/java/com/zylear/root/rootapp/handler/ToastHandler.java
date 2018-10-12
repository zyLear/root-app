package com.zylear.root.rootapp.handler;


import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;


public class ToastHandler extends Handler {

    private static ToastHandler instance;

    private Activity activity;
    private Integer time;


    public void show(Activity activity, String content, Integer time) {

        this.activity = activity;
        this.time = time;

        Message message = new Message();
        message.what = 0;
        message.obj = content;
        instance.sendMessage(message);

    }


    @Override
    public void handleMessage(Message msg) {
        Toast.makeText(activity, (String) msg.obj, time).show();
    }

    public static ToastHandler getInstance() {
        return instance;
    }

    public static void setInstance(ToastHandler instance) {
        if (ToastHandler.instance == null) {
            synchronized (ToastHandler.class) {
                if (ToastHandler.instance == null) {
                    ToastHandler.instance = instance;
                }
            }
        }
    }
}
