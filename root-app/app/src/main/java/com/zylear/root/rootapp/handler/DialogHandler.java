package com.zylear.root.rootapp.handler;


import android.app.Activity;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;


public class DialogHandler extends Handler {

    private static DialogHandler instance;

    private Activity activity;


    public void show(Activity activity, String content) {

        this.activity = activity;

        Message message = new Message();
        message.what = 0;
        message.obj = content;
        instance.sendMessage(message);

    }


    @Override
    public void handleMessage(Message msg) {
        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setTitle("提示")//设置对话框的标题
                .setMessage((String) msg.obj)//设置对话框的内容
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    public static DialogHandler getInstance() {
        return instance;
    }

    public static void setInstance(DialogHandler instance) {
        if (DialogHandler.instance == null) {
            synchronized (DialogHandler.class) {
                if (DialogHandler.instance == null) {
                    DialogHandler.instance = instance;
                }
            }
        }
    }
}
