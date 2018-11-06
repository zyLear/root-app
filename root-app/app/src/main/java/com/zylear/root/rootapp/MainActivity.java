package com.zylear.root.rootapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.zylear.root.rootapp.bean.AppCache;
import com.zylear.root.rootapp.bean.BaseResponse;
import com.zylear.root.rootapp.bean.LoginRequest;
import com.zylear.root.rootapp.bean.LoginResponse;
import com.zylear.root.rootapp.constants.AppConstant;
import com.zylear.root.rootapp.handler.DialogHandler;
import com.zylear.root.rootapp.handler.ToastHandler;
import com.zylear.root.rootapp.util.DeviceUniqueIdUtil;
import com.zylear.root.rootapp.util.ExternalPermissionUtil;
import com.zylear.root.rootapp.util.JsonUtil;
import com.zylear.root.rootapp.util.OkHttpUtil;
import com.zylear.root.rootapp.util.SharedPreferencesUtil;
import com.zylear.root.rootapp.util.StringUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((TextView) findViewById(R.id.mainText)).setText(getResources().getString(R.string.app_name) + "\n" + AppConstant.VERSION);
        setTitle(getResources().getString(R.string.app_name) + "   " + AppConstant.VERSION);
        ToastHandler.setInstance(new ToastHandler());
        DialogHandler.setInstance(new DialogHandler());


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                    String autoLogin = SharedPreferencesUtil.read(MainActivity.this, "autoLogin");
                    if ("true".equals(autoLogin)) {
                        login();
                    } else {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
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

                        String deviceId = DeviceUniqueIdUtil.getDeviceId(MainActivity.this);
                        if (!StringUtil.isEmpty(deviceId)) {

                            LoginRequest loginRequest = new LoginRequest();
                            loginRequest.setAccount(accountText);
                            loginRequest.setPassword(passwordText);
                            loginRequest.setDeviceId(deviceId);
                            String url = AppConstant.HOST + AppConstant.LOGIN;
                            String content = OkHttpUtil.syncExeJsonPostGetString(url, JsonUtil.toJSONString(loginRequest));
                            LoginResponse response = JsonUtil.getObjectFromJson(content, LoginResponse.class);
                            if (BaseResponse.isSuccess(response)) {
                                AppCache.account = accountText;
                                AppCache.password = passwordText;
                                AppCache.accountInfo = response.getAccountInfo();
                                AppCache.helper = response.getHelper();
                                AppCache.vipHelper = response.getVipHelper();
                                Intent intent = new Intent(MainActivity.this, ControlPanelActivity.class);
                                startActivity(intent);
                                return;
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }.start();
        }

    }




}




