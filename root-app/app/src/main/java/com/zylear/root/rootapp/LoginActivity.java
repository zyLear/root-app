package com.zylear.root.rootapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.zylear.root.rootapp.bean.AppCache;
import com.zylear.root.rootapp.bean.BaseResponse;
import com.zylear.root.rootapp.bean.LoginRequest;
import com.zylear.root.rootapp.bean.LoginResponse;
import com.zylear.root.rootapp.constants.AppConstant;
import com.zylear.root.rootapp.handler.ToastHandler;
import com.zylear.root.rootapp.util.DeviceUniqueIdUtil;
import com.zylear.root.rootapp.util.ExternalPermissionUtil;
import com.zylear.root.rootapp.util.JsonUtil;
import com.zylear.root.rootapp.util.OkHttpUtil;
import com.zylear.root.rootapp.util.SharedPreferencesUtil;
import com.zylear.root.rootapp.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private Button sureLogin;
    private Button register;
    private EditText account;
    private EditText password;
    private CheckBox autoLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle(getResources().getString(R.string.app_name) + "   " + AppConstant.VERSION);

        applyPermission();
        sureLogin = findViewById(R.id.sureLogin);
        account = findViewById(R.id.loginAccount);
        password = findViewById(R.id.loginPassword);
        register = findViewById(R.id.register);
        autoLogin = findViewById(R.id.autoLogin);

        sureLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sureLogin();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
    }

    private void register() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    private void sureLogin() {


        final String accountText = account.getText().toString();
        final String passwordText = password.getText().toString();
        Log.d("dev", "aaa: " + accountText + "  " + passwordText);

        if (StringUtil.isEmpty(accountText) || StringUtil.isEmpty(passwordText)
                || !accountText.matches("[a-zA-Z0-9]{6,16}")
                || !passwordText.matches("[a-zA-Z0-9]{6,16}")) {
            ToastHandler.getInstance().show(this, "请输入6到16位字母或数字！！！", Toast.LENGTH_SHORT);
            return;
        }

        new Thread() {
            @Override
            public void run() {

                try {
                    String deviceId = DeviceUniqueIdUtil.getDeviceId(LoginActivity.this);
                    if (StringUtil.isEmpty(deviceId)) {
                        ToastHandler.getInstance().show(LoginActivity.this, "登录失败，获取设备id出错！", Toast.LENGTH_SHORT);
                        return;
                    }

                    LoginRequest loginRequest = new LoginRequest();
                    loginRequest.setAccount(accountText);
                    loginRequest.setPassword(passwordText);
                    loginRequest.setDeviceId(deviceId);
                    String url = AppConstant.HOST + AppConstant.LOGIN;
                    String content = OkHttpUtil.syncExeJsonPostGetString(url, JsonUtil.toJSONString(loginRequest));
                    LoginResponse response = JsonUtil.getObjectFromJson(content, LoginResponse.class);
                    if (BaseResponse.isSuccess(response)) {
                        loginSuccess(autoLogin.isChecked(), accountText, passwordText, response);
                        Intent intent = new Intent(LoginActivity.this, ControlPanelActivity.class);
                        startActivity(intent);
                    } else {
                        ToastHandler.getInstance().show(LoginActivity.this, "账号密码错误！", Toast.LENGTH_SHORT);
                    }


                } catch (Exception e) {
                    ToastHandler.getInstance().show(LoginActivity.this, "网络出错！", Toast.LENGTH_SHORT);
                    e.printStackTrace();
                }

            }
        }.start();


    }


    private void loginSuccess(Boolean autoLogin, String accountText, String passwordText, LoginResponse loginResponse) {

        AppCache.account = accountText;
        AppCache.password = passwordText;
        AppCache.accountInfo = loginResponse.getAccountInfo();
        AppCache.helper = loginResponse.getHelper();
        AppCache.vipHelper = loginResponse.getVipHelper();

        if (autoLogin) {
            Log.d("dev", "check ");
            Map<String, String> map = new HashMap<>();
            map.put("autoLogin", "true");
            map.put("account", accountText);
            map.put("password", passwordText);
            SharedPreferencesUtil.write(this, map);
        } else {
            Log.d("dev", "no check ");
            Map<String, String> map = new HashMap<>();
            map.put("autoLogin", "false");
            map.put("account", "");
            map.put("password", "");
            SharedPreferencesUtil.write(this, map);
        }
    }

    public void onBackPressed() {

    }

    private void applyPermission() {
        ExternalPermissionUtil.verifyStoragePermissions(LoginActivity.this);
        try {
            Runtime.getRuntime().exec("su\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
