package com.zylear.root.rootapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.zylear.root.rootapp.bean.BaseResponse;
import com.zylear.root.rootapp.bean.LoginRequest;
import com.zylear.root.rootapp.constants.AppConstant;
import com.zylear.root.rootapp.handler.ToastHandler;
import com.zylear.root.rootapp.util.JsonUtil;
import com.zylear.root.rootapp.util.OkHttpUtil;
import com.zylear.root.rootapp.util.SharedPreferencesUtil;
import com.zylear.root.rootapp.util.StringUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ToastHandler.setInstance(new ToastHandler());


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    String autoLogin = SharedPreferencesUtil.read(MainActivity.this, "autoLogin");
                    if ("true".equals(autoLogin)) {
                        login();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

    private void login() {

        final String accountText = SharedPreferencesUtil.read(MainActivity.this, "account");
        final String passwordText = SharedPreferencesUtil.read(MainActivity.this, "password");

        if (StringUtil.isEmpty(accountText) || StringUtil.isEmpty(passwordText)) {

            //
        } else {
            new Thread() {
                @Override
                public void run() {

                    try {
                        LoginRequest loginRequest = new LoginRequest();
                        loginRequest.setAccount(accountText);
                        loginRequest.setPassword(passwordText);
                        String url = AppConstant.HOST + AppConstant.LOGIN;
                        String content = OkHttpUtil.syncExeJsonPostGetString(url, JsonUtil.toJSONString(loginRequest));
                        BaseResponse baseResponse = JsonUtil.getObjectFromJson(content, BaseResponse.class);
                        if (BaseResponse.isSuccess(baseResponse)) {
                            Intent intent = new Intent(MainActivity.this, ControlPanelActivity.class);
                            startActivity(intent);
                            return;
                        } else {
                            ToastHandler.getInstance().show(MainActivity.this, "账号密码错误！", Toast.LENGTH_SHORT);
                        }


                    } catch (Exception e) {
                        ToastHandler.getInstance().show(MainActivity.this, "网络出错！", Toast.LENGTH_SHORT);
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }.start();
        }

    }


}




