package com.zylear.root.rootapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.zylear.root.rootapp.bean.AppCache;
import com.zylear.root.rootapp.bean.BaseResponse;
import com.zylear.root.rootapp.bean.PassCheckRequest;
import com.zylear.root.rootapp.bean.PassCheckResponse;
import com.zylear.root.rootapp.constants.AppConstant;
import com.zylear.root.rootapp.handler.ToastHandler;
import com.zylear.root.rootapp.util.ExternalPermissionUtil;
import com.zylear.root.rootapp.util.JsonUtil;
import com.zylear.root.rootapp.util.OkHttpUtil;
import com.zylear.root.rootapp.util.SharedPreferencesUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class ControlPanelActivity extends AppCompatActivity {


    private Button apply;
    private Button changeBrand;
    private Button recoverBrand;
    private Button startPassCheck;
    private Button stopPassCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_panel);

        applyPermission();

        apply = findViewById(R.id.apply);

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> map = new HashMap<>();
                map.put("zy", "yesssssssssss");
                map.put("xxx", "xxxxxxxxxxxxx");
                SharedPreferencesUtil.write(ControlPanelActivity.this, map);
                Log.d("dev", "read :" + SharedPreferencesUtil.read(ControlPanelActivity.this, "zy"));
                Log.d("dev", "read :" + SharedPreferencesUtil.read(ControlPanelActivity.this, "xxx"));

            }
        });

        changeBrand = findViewById(R.id.changeBrand);

        changeBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeBrand();
            }
        });

        recoverBrand = findViewById(R.id.recoverBrand);

        recoverBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                recoverBrand();
            }
        });

        startPassCheck = findViewById(R.id.startPassCheck);

        startPassCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startPassCheck();
            }
        });

        stopPassCheck = findViewById(R.id.stopPassCheck);

        stopPassCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stopPassCheck();
            }
        });
    }

    private void applyPermission() {
        ExternalPermissionUtil.verifyStoragePermissions(ControlPanelActivity.this);
        try {
            Runtime.getRuntime().exec("su");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void recoverBrand() {

        DataOutputStream outputStream = null;
        try {
            Process exec = Runtime.getRuntime().exec("su");
            outputStream = new DataOutputStream(exec.getOutputStream());
            outputStream.writeBytes("mount -o remount rw /\n");
            outputStream.writeBytes("mount -o remount rw /system\n");
            if (new File("/sdcard/init_old.sh").exists()) {
                outputStream.writeBytes("cp /sdcard/init_old.sh /etc/init.sh\n");
            }
            if (new File("/sdcard/build_old.prop").exists()) {
                outputStream.writeBytes("cp /sdcard/build_old.prop /system/build.prop\n");
            }

//            Process exec = Runtime.getRuntime().exec("su");
//            outputStream = new DataOutputStream(exec.getOutputStream());
//            outputStream.writeBytes("mount -o remount rw /\n");
//            outputStream.writeBytes("mount -o remount rw /system\n");
//            if (!new File("/sdcard/init_old.sh").exists()) {
//                outputStream.writeBytes("cp /etc/init.sh /sdcard/init_old.sh\n");
//            }
//            if (!new File("/sdcard/build_old.prop").exists()) {
//                outputStream.writeBytes("cp /system/build.prop /sdcard/build_old.prop\n");
//            }
//            pullFile();
//            outputStream.writeBytes("cp /sdcard/build_pull.prop /system/build.prop\n");
//            outputStream.writeBytes("chmod 0644 /system/build.prop\n");
//            outputStream.writeBytes("cp /sdcard/init_pull.sh /etc/init.sh\n");
//            outputStream.writeBytes("chmod 0755 /etc/init.sh\n");


        } catch (Exception e) {
//                    Toast.makeText(this, "修改机型失败！！！！", Toast.LENGTH_LONG).show();
            Log.e("dev", "requirePermission:  error", e);
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void changeBrand() {

        new Thread() {
            @Override
            public void run() {
                DataOutputStream outputStream = null;
                try {
                    Process exec = Runtime.getRuntime().exec("su");
                    outputStream = new DataOutputStream(exec.getOutputStream());
                    outputStream.writeBytes("mount -o remount rw /\n");
                    outputStream.writeBytes("mount -o remount rw /system\n");
                    if (!new File("/sdcard/init_old.sh").exists()) {
                        outputStream.writeBytes("cp /etc/init.sh /sdcard/init_old.sh\n");
                    }
                    if (!new File("/sdcard/build_old.prop").exists()) {
                        outputStream.writeBytes("cp /system/build.prop /sdcard/build_old.prop\n");
                    }
                    pullFile();
                    outputStream.writeBytes("cp /sdcard/build_pull.prop /system/build.prop\n");
                    outputStream.writeBytes("chmod 0644 /system/build.prop\n");
                    outputStream.writeBytes("cp /sdcard/init_pull.sh /etc/init.sh\n");
                    outputStream.writeBytes("chmod 0755 /etc/init.sh\n");

                } catch (Exception e) {
//                    Toast.makeText(this, "修改机型失败！！！！", Toast.LENGTH_LONG).show();
                    Log.e("dev", "requirePermission:  error", e);
                } finally {
                    try {
                        if (outputStream != null) {
                            outputStream.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

    }

    private void pullFile() {
        BufferedWriter fileWriter = null;
        BufferedWriter buildWriter = null;
        try {
            PassCheckRequest passCheckRequest = new PassCheckRequest(AppCache.account, AppCache.password, AppConstant.INIT_SH);
            String param = JsonUtil.toJSONString(passCheckRequest);
            String content = OkHttpUtil.syncExeJsonPostGetString(AppConstant.HOST + AppConstant.PULL_PASS_CHECK_CONTENT_URI, param);
            System.out.println("init content:    " + content);
            File init = new File("/sdcard/init_pull.sh");
            if (!init.exists()) {
                init.createNewFile();
            }
            fileWriter = new BufferedWriter(new FileWriter(init));
            fileWriter.write(content);
//
            passCheckRequest = new PassCheckRequest(AppCache.account, AppCache.password, AppConstant.BUILD_PROP);
            param = JsonUtil.toJSONString(passCheckRequest);
            content = OkHttpUtil.syncExeJsonPostGetString(AppConstant.HOST + AppConstant.PULL_PASS_CHECK_CONTENT_URI, param);
            System.out.println("build content:    " + content);

            File build = new File("/sdcard/build_pull.prop");
            if (!build.exists()) {
                build.createNewFile();
            }
            buildWriter = new BufferedWriter(new FileWriter(build));
            buildWriter.write(content);
        } catch (Exception e) {
            Log.e("dev", "requirePermission:  error", e);
        } finally {
            try {
                if (buildWriter != null) {
                    buildWriter.close();
                }
                if (fileWriter != null) {
                    fileWriter.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void stopPassCheck() {

        new Thread() {
            @Override
            public void run() {
                DataOutputStream outputStream = null;
                try {
                    Process exec = Runtime.getRuntime().exec("su");
                    BufferedReader br = new BufferedReader(new InputStreamReader(exec.getInputStream()));

                    PassCheckRequest passCheckRequest = new PassCheckRequest(AppCache.account, AppCache.password, AppConstant.STOP_PASS_CHECK);
                    String param = JsonUtil.toJSONString(passCheckRequest);
                    String content = OkHttpUtil.syncExeJsonPostGetString(AppConstant.HOST + AppConstant.PULL_PASS_CHECK_CONTENT_URI, param);
                    System.out.println("content:    " + content);



                    outputStream = new DataOutputStream(exec.getOutputStream());
                    outputStream.writeBytes(content);
                    new Run(br).start();
                } catch (Exception e) {
                    Log.e("dev", "stopPassCheck:  error", e);
                    ToastHandler.getInstance().show(ControlPanelActivity.this, "关闭过检测失败！！！", Toast.LENGTH_LONG);
                } finally {
                    try {
                        outputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    private void startPassCheck() {
        new Thread() {
            @Override
            public void run() {
                DataOutputStream outputStream = null;
                try {
                    Process exec = Runtime.getRuntime().exec("su");
                    BufferedReader br = new BufferedReader(new InputStreamReader(exec.getInputStream()));

                    PassCheckRequest passCheckRequest = new PassCheckRequest(AppCache.account, AppCache.password, AppConstant.PASS_CHECK);
                    String param = JsonUtil.toJSONString(passCheckRequest);
                    String content = OkHttpUtil.syncExeJsonPostGetString(AppConstant.HOST + AppConstant.PULL_PASS_CHECK_CONTENT_URI, param);
                    System.out.println("content:    " + content);

                    PassCheckResponse response = JsonUtil.getObjectFromJson(content, PassCheckResponse.class);

                    if (BaseResponse.isSuccess(response)) {
                        
                    }

                    new Run(br).start();
                    outputStream = new DataOutputStream(exec.getOutputStream());
                    outputStream.writeBytes(content);

                } catch (Exception e) {
                    Log.e("dev", "startPassCheck:  error", e);
                    ToastHandler.getInstance().show(ControlPanelActivity.this, "开启过检测失败！！！", Toast.LENGTH_LONG);
                } finally {
                    try {
                        outputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }


    class Run extends Thread {
        BufferedReader br;

        Run(BufferedReader br) {
            this.br = br;
        }

        @Override
        public void run() {
            String string;
            try {
                System.out.println("eeeeeeewwwwwwwwwwwwwwwww :");
                while ((string = br.readLine()) != null) {
                    Log.d("dev", " rererererere :" + string);
                }

                Log.e("dev", "eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee close s ssssssss");
            } catch (IOException e) {
                Log.e("dev", " close s ssssssss");
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


}
