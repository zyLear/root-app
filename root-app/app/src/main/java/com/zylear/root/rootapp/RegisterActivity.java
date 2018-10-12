package com.zylear.root.rootapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.zylear.root.rootapp.bean.BaseResponse;
import com.zylear.root.rootapp.bean.LoginRequest;
import com.zylear.root.rootapp.bean.RegisterRequest;
import com.zylear.root.rootapp.constants.AppConstant;
import com.zylear.root.rootapp.handler.ToastHandler;
import com.zylear.root.rootapp.util.DeviceUniqueIdUtil;
import com.zylear.root.rootapp.util.JsonUtil;
import com.zylear.root.rootapp.util.OkHttpUtil;
import com.zylear.root.rootapp.util.SharedPreferencesUtil;
import com.zylear.root.rootapp.util.StringUtil;

public class RegisterActivity extends AppCompatActivity {

    Button back;
    Button register;
    EditText account;
    EditText password;
    EditText surePassowrd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        account = findViewById(R.id.registerAccount);
        password = findViewById(R.id.registerPassword);
        surePassowrd = findViewById(R.id.registerSurePassword);
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
        final String surePasswordText = surePassowrd.getText().toString();

        if (!passwordText.equals(surePasswordText)) {
            ToastHandler.getInstance().show(RegisterActivity.this, "注册失败，两次输入密码不一致！", Toast.LENGTH_SHORT);
        }

        new Thread() {
            @Override
            public void run() {

                try {

                    String deviceId = SharedPreferencesUtil.read(RegisterActivity.this, "deviceId");

                    if (StringUtil.isEmpty(deviceId)) {
                        deviceId = DeviceUniqueIdUtil.getUniquePsuedoID();
                        if (deviceId == null) {
                            ToastHandler.getInstance().show(RegisterActivity.this, "注册失败，获取设备id出错！", Toast.LENGTH_SHORT);
                            return;
                        }
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

                    } else if (baseResponse.getErrorCode().equals(BaseResponse.DEVICE_EXIST)) {
                        ToastHandler.getInstance().show(RegisterActivity.this, "注册失败，此设备已经注册！", Toast.LENGTH_SHORT);
                    } else if (baseResponse.getErrorCode().equals(BaseResponse.DEVICE_EXIST)) {
                        ToastHandler.getInstance().show(RegisterActivity.this, "注册失败，此账号已经注册！", Toast.LENGTH_SHORT);
                    }


                } catch (Exception e) {
                    ToastHandler.getInstance().show(RegisterActivity.this, "网络出错！", Toast.LENGTH_SHORT);
                    e.printStackTrace();
                }

            }
        }.start();


    }

    private void back() {
    }
}
