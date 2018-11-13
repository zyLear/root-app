package com.zylear.root.rootapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zylear.root.rootapp.bean.ActivateRequest;
import com.zylear.root.rootapp.bean.AppCache;
import com.zylear.root.rootapp.bean.BaseResponse;
import com.zylear.root.rootapp.constants.AppConstant;
import com.zylear.root.rootapp.handler.ToastHandler;
import com.zylear.root.rootapp.util.DeviceUniqueIdUtil;
import com.zylear.root.rootapp.util.JsonUtil;
import com.zylear.root.rootapp.util.OkHttpUtil;
import com.zylear.root.rootapp.util.StringUtil;

public class ActivateActivity extends AppCompatActivity {

    private Button sureActivate;
    private EditText cardNumber;
    private EditText cardPassword;
    private Button back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate);
        setTitle(getResources().getString(R.string.app_name) + "   " + AppConstant.VERSION);

        sureActivate = findViewById(R.id.sureActivate);
        cardNumber = findViewById(R.id.cardNumber);
        cardPassword = findViewById(R.id.cardPassword);


        sureActivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sureActivate();
            }
        });


        back = findViewById(R.id.backFromActivate);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivateActivity.this.finish();
            }
        });
    }


    private void sureActivate() {


        final String cardNumberText = cardNumber.getText().toString();
        final String cardPasswordText = cardPassword.getText().toString();

        if (StringUtil.isEmpty(cardNumberText) || StringUtil.isEmpty(cardPasswordText)
                || !cardNumberText.matches("[a-zA-Z0-9]{6,16}")
                || !cardPasswordText.matches("[a-zA-Z0-9]{6,16}")) {
            ToastHandler.getInstance().show(this, "请输入6到16位字母或数字！！！", Toast.LENGTH_SHORT);
            return;
        }

        new Thread() {
            @Override
            public void run() {

                try {
                    String deviceId = DeviceUniqueIdUtil.getDeviceId(ActivateActivity.this);
                    if (StringUtil.isEmpty(deviceId)) {
                        ToastHandler.getInstance().show(ActivateActivity.this, "开通VIP失败，获取设备id出错！", Toast.LENGTH_SHORT);
                        return;
                    }

                    if (StringUtil.isEmpty(AppCache.account)) {
                        ToastHandler.getInstance().show(ActivateActivity.this, "开通VIP失败，获取账号出错！", Toast.LENGTH_SHORT);
                        return;
                    }
                    ActivateRequest activateRequest = new ActivateRequest();
                    activateRequest.setAccount(AppCache.account);
                    activateRequest.setCardNumber(cardNumberText);
                    activateRequest.setCardPassword(cardPasswordText);
                    activateRequest.setDeviceId(deviceId);
                    String url = AppConstant.HOST + AppConstant.ACTIVATE_CARD;
                    String content = OkHttpUtil.syncExeJsonPostGetString(url, JsonUtil.toJSONString(activateRequest));
                    BaseResponse response = JsonUtil.getObjectFromJson(content, BaseResponse.class);
                    if (BaseResponse.isSuccess(response)) {
                        ToastHandler.getInstance().show(ActivateActivity.this, "开通VIP成功，请重新登录使用", Toast.LENGTH_SHORT);
                    } else {
                        ToastHandler.getInstance().show(ActivateActivity.this, "开通VIP" + response.getErrorMessage(), Toast.LENGTH_SHORT);
                    }


                } catch (Exception e) {
                    ToastHandler.getInstance().show(ActivateActivity.this, "网络出错！", Toast.LENGTH_SHORT);
                    e.printStackTrace();
                }

            }
        }.start();

    }


}
