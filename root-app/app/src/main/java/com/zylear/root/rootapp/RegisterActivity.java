package com.zylear.root.rootapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zylear.root.rootapp.bean.BaseResponse;
import com.zylear.root.rootapp.bean.RegisterRequest;
import com.zylear.root.rootapp.constants.AppConstant;
import com.zylear.root.rootapp.handler.ToastHandler;
import com.zylear.root.rootapp.util.DeviceUniqueIdUtil;
import com.zylear.root.rootapp.util.JsonUtil;
import com.zylear.root.rootapp.util.OkHttpUtil;
import com.zylear.root.rootapp.util.SharedPreferencesUtil;
import com.zylear.root.rootapp.util.StringUtil;

public class RegisterActivity extends AppCompatActivity {

    private Button back;
    private Button register;
    private EditText account;
    private EditText password;
    private EditText surePassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle(getResources().getString(R.string.app_name) + "   " + AppConstant.VERSION);


        account = findViewById(R.id.registerAccount);
        password = findViewById(R.id.registerPassword);
        surePassword = findViewById(R.id.registerSurePassword);
        back = findViewById(R.id.backFromRegister);
        register = findViewById(R.id.sureRegister);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
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
        final String accountText = account.getText().toString();
        final String passwordText = password.getText().toString();
        final String surePasswordText = surePassword.getText().toString();

        if (!passwordText.equals(surePasswordText)) {
            ToastHandler.getInstance().show(RegisterActivity.this, "注册失败，两次输入密码不一致！", Toast.LENGTH_SHORT);
            return;
        }

        if (StringUtil.isEmpty(accountText) || StringUtil.isEmpty(passwordText)
                || !accountText.matches("[a-zA-Z0-9]{6,16}")
                || !passwordText.matches("[a-zA-Z0-9]{6,16}")
               /* || accountText.length() < 6 || accountText.length() > 16
                || passwordText.length() < 6 || passwordText.length() > 16*/) {
            ToastHandler.getInstance().show(this, "请输入6到16位字母或数字！！！", Toast.LENGTH_SHORT);
            return;
        }


        new Thread() {
            @Override
            public void run() {

                try {

                    String deviceId = DeviceUniqueIdUtil.getDeviceId(RegisterActivity.this);
                    if (StringUtil.isEmpty(deviceId)) {
                        ToastHandler.getInstance().show(RegisterActivity.this, "注册失败，获取设备id出错！", Toast.LENGTH_SHORT);
                        return;
                    }
                    Log.d("dev", "deviceId : " + deviceId);

                    RegisterRequest registerRequest = new RegisterRequest();
                    registerRequest.setAccount(accountText);
                    registerRequest.setPassword(passwordText);
                    registerRequest.setDeviceId(deviceId);

                    String url = AppConstant.HOST + AppConstant.REGISTER;
                    String content = OkHttpUtil.syncExeJsonPostGetString(url, JsonUtil.toJSONString(registerRequest));
                    BaseResponse baseResponse = JsonUtil.getObjectFromJson(content, BaseResponse.class);
                    if (BaseResponse.isSuccess(baseResponse)) {
                        ToastHandler.getInstance().show(RegisterActivity.this, "注册成功!", Toast.LENGTH_SHORT);

                    } else {
                        ToastHandler.getInstance().show(RegisterActivity.this, baseResponse.getErrorMessage(), Toast.LENGTH_SHORT);
                    }


                } catch (Exception e) {
                    ToastHandler.getInstance().show(RegisterActivity.this, "网络出错！", Toast.LENGTH_SHORT);
                    e.printStackTrace();
                }

            }
        }.start();


    }

    public void onBackPressed() {

    }


    private void back() {
        RegisterActivity.this.finish();
    }
}
