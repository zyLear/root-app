package com.zylear.root.rootapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.zylear.root.rootapp.bean.PassCheckBean;
import com.zylear.root.rootapp.constants.AppConstant;
import com.zylear.root.rootapp.util.ExternalPermissionUtil;
import com.zylear.root.rootapp.util.JsonUtil;
import com.zylear.root.rootapp.util.OkHttpUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class ControlPanelActivity extends AppCompatActivity {

    private Handler toastHandler;

    private Button apply;
    private Button changeBrand;
    private Button recoverBrand;
    private Button startPassCheck;
    private Button stopPassCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_panel);

        apply = findViewById(R.id.apply);

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExternalPermissionUtil.verifyStoragePermissions(ControlPanelActivity.this);
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
            PassCheckBean passCheckBean = new PassCheckBean("admin", "admin", AppConstant.INIT_SH);
            String param = JsonUtil.toJSONString(passCheckBean);
            String content = OkHttpUtil.syncExeJsonPostGetString(AppConstant.HOST + AppConstant.PULL_PASS_CHECK_CONTENT_URI, param);
            System.out.println("init content:    " + content);
            File init = new File("/sdcard/init_pull.sh");
            if (!init.exists()) {
                init.createNewFile();
            }
            fileWriter = new BufferedWriter(new FileWriter(init));
            fileWriter.write(content);
//
            passCheckBean = new PassCheckBean("admin", "admin", AppConstant.BUILD_PROP);
            param = JsonUtil.toJSONString(passCheckBean);
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

                    PassCheckBean passCheckBean = new PassCheckBean("admin", "admin", AppConstant.STOP_PASS_CHECK);
                    String param = JsonUtil.toJSONString(passCheckBean);
                    String content = OkHttpUtil.syncExeJsonPostGetString(AppConstant.HOST + AppConstant.PULL_PASS_CHECK_CONTENT_URI, param);
                    System.out.println("content:    " + content);

                    outputStream = new DataOutputStream(exec.getOutputStream());
                    outputStream.writeBytes(content);
                    new Run(br).start();
                } catch (Exception e) {
                    Log.e("dev", "requirePermission:  error", e);
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

                    PassCheckBean passCheckBean = new PassCheckBean("admin", "admin", AppConstant.PASS_CHECK);
                    String param = JsonUtil.toJSONString(passCheckBean);
                    String content = OkHttpUtil.syncExeJsonPostGetString(AppConstant.HOST + AppConstant.PULL_PASS_CHECK_CONTENT_URI, param);
                    System.out.println("content:    " + content);
                    new Run(br).start();
                    outputStream = new DataOutputStream(exec.getOutputStream());
                    outputStream.writeBytes(content);
                } catch (Exception e) {
                    Log.e("dev", "requirePermission:  error", e);
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


    class ToastHandler extends Handler{

        @Override
        public void handleMessage(Message msg) {
//           msg.getData().
        }
    }
}
