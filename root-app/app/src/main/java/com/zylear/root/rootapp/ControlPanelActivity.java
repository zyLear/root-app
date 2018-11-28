package com.zylear.root.rootapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zylear.root.rootapp.bean.AppCache;
import com.zylear.root.rootapp.bean.BaseResponse;
import com.zylear.root.rootapp.bean.PassCheckRequest;
import com.zylear.root.rootapp.bean.PassCheckResponse;
import com.zylear.root.rootapp.constants.AppConstant;
import com.zylear.root.rootapp.handler.DialogHandler;
import com.zylear.root.rootapp.handler.ToastHandler;
import com.zylear.root.rootapp.util.DeviceUniqueIdUtil;
import com.zylear.root.rootapp.util.ExternalPermissionUtil;
import com.zylear.root.rootapp.util.JsonUtil;
import com.zylear.root.rootapp.util.OkHttpUtil;
import com.zylear.root.rootapp.util.SharedPreferencesUtil;
import com.zylear.root.rootapp.util.StringUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ControlPanelActivity extends AppCompatActivity {


    private Button helper;
    private Button changeBrand;
    private Button buyVip;
    private Button activate;
    //    private Button recoverBrand;
    private Button startPassCheck;
    private Button stopPassCheck;
//    private Button v2StartPassCheck;
//    private Button v2StopPassCheck;
    private Button startPassCheck2;
    private Button logout;

    private TextView notice;


    private int sign = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_panel);
        setTitle(getResources().getString(R.string.app_name) + "   " + AppConstant.VERSION);


        applyPermission();
        notice = findViewById(R.id.notice);

        buyVip = findViewById(R.id.buyVip);
        buyVip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buyVip();
            }
        });


        activate = findViewById(R.id.activate);
        activate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ControlPanelActivity.this, ActivateActivity.class);
                startActivity(intent);
            }
        });


        logout = findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> map = new HashMap<>();
                map.put("autoLogin", "false");
                map.put("account", "");
                map.put("password", "");
                SharedPreferencesUtil.write(ControlPanelActivity.this, map);
                Intent integer = new Intent(ControlPanelActivity.this, LoginActivity.class);
                startActivity(integer);
            }
        });


        helper = findViewById(R.id.helper);

        helper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ControlPanelActivity.this, HelperActivity.class);
                startActivity(intent);
            }
        });

        changeBrand = findViewById(R.id.changeBrand);

        changeBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeBrand();
            }
        });

//        recoverBrand = findViewById(R.id.recoverBrand);
//
//        recoverBrand.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                recoverBrand();
//            }
//        });

        startPassCheck = findViewById(R.id.startPassCheck);

        startPassCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startPassCheck(AppConstant.PASS_CHECK);
            }
        });

        stopPassCheck = findViewById(R.id.stopPassCheck);

        stopPassCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stopPassCheck(AppConstant.STOP_PASS_CHECK);
            }
        });

        startPassCheck2 = findViewById(R.id.startPassCheck2);

        startPassCheck2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPassCheck(AppConstant.PASS_CHECK2);
            }
        });


//        v2StartPassCheck = findViewById(R.id.v2StartPassCheck);
//        v2StartPassCheck.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startPassCheck(AppConstant.V2_PASS_CHECK);
//            }
//        });
//
//        v2StopPassCheck = findViewById(R.id.v2StopPassCheck);
//        v2StopPassCheck.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                stopPassCheck(AppConstant.V2_STOP_PASS_CHECK);
//            }
//        });

        checkFirstEnter();
    }

    private void buyVip() {
        DialogHandler.getInstance().show(this, AppCache.vipHelper);
    }

    private void checkFirstEnter() {
        if (sign > 0) {
            sign--;
            Intent intent = new Intent(this, HelperActivity.class);
            startActivity(intent);
        }
    }

    private void applyPermission() {
        ExternalPermissionUtil.verifyStoragePermissions(ControlPanelActivity.this);
        try {
            Runtime.getRuntime().exec("su\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void recoverBrand() {

        new Thread() {
            @Override
            public void run() {
                DataOutputStream outputStream = null;
                try {
                    String deviceId = DeviceUniqueIdUtil.getDeviceId(ControlPanelActivity.this);
                    if (StringUtil.isEmpty(deviceId)) {
                        ToastHandler.getInstance().show(ControlPanelActivity.this, "恢复机型失败，获取设备id出错！", Toast.LENGTH_SHORT);
                        return;
                    }

                    Process exec = Runtime.getRuntime().exec("su\n");
                    outputStream = new DataOutputStream(exec.getOutputStream());
                    outputStream.writeBytes("mount -o remount rw /\n");
                    outputStream.writeBytes("mount -o remount rw /system\n");
                    if (new File("/sdcard/init_old.sh").exists()) {
                        outputStream.writeBytes("cp /sdcard/init_old.sh /etc/init.sh\n");
                    }
                    if (new File("/sdcard/build_old.prop").exists()) {
                        outputStream.writeBytes("cp /sdcard/build_old.prop /system/build.prop\n");
                    }
                    new Thread() {
                        @Override
                        public void run() {
                            if (checkBrand("/sdcard/init_old.sh", "/etc/init.sh") && checkBrand("/sdcard/build_old.prop", "/system/build.prop")) {
                                ToastHandler.getInstance().show(ControlPanelActivity.this, "恢复机型成功！", Toast.LENGTH_SHORT);
                            } else {
                                ToastHandler.getInstance().show(ControlPanelActivity.this, "未知错误！恢复机型失败！", Toast.LENGTH_SHORT);
                            }
                        }
                    }.start();

                } catch (Exception e) {
//                    Toast.makeText(this, "修改机型失败！！！！", Toast.LENGTH_LONG).show();
                    Log.e("dev", "requirePermission:  error", e);
                    ToastHandler.getInstance().show(ControlPanelActivity.this, "恢复机型失败！请检查网络和ROOT权限！", Toast.LENGTH_SHORT);

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


    private void changeBrand() {

        new Thread() {
            @Override
            public void run() {
                DataOutputStream outputStream = null;
                try {
                    String deviceId = DeviceUniqueIdUtil.getDeviceId(ControlPanelActivity.this);
                    if (StringUtil.isEmpty(deviceId)) {
                        ToastHandler.getInstance().show(ControlPanelActivity.this, "修改机型失败，获取设备id出错！", Toast.LENGTH_SHORT);
                        return;
                    }

                    if (pullFile(deviceId)) {
                        Process exec = Runtime.getRuntime().exec("su\n");
                        outputStream = new DataOutputStream(exec.getOutputStream());
                        outputStream.writeBytes("mount -o remount rw /\n");
                        outputStream.writeBytes("mount -o remount rw /system\n");
                        if (!new File("/sdcard/init_old.sh").exists()) {
                            outputStream.writeBytes("cp /etc/init.sh /sdcard/init_old.sh\n");
                        }
                        if (!new File("/sdcard/build_old.prop").exists()) {
                            outputStream.writeBytes("cp /system/build.prop /sdcard/build_old.prop\n");
                        }

                        outputStream.writeBytes("cp /sdcard/build_pull.prop /system/build.prop\n");
                        outputStream.writeBytes("chmod 0644 /system/build.prop\n");
                        outputStream.writeBytes("cp /sdcard/init_pull.sh /etc/init.sh\n");
                        outputStream.writeBytes("chmod 0755 /etc/init.sh\n");

                        new Thread() {
                            @Override
                            public void run() {
                                if (checkBrand("/sdcard/build_pull.prop", "/system/build.prop") && checkBrand("/sdcard/init_pull.sh", "/etc/init.sh")) {
//                                    ToastHandler.getInstance().show(ControlPanelActivity.this, "修改机型成功！", Toast.LENGTH_SHORT);
                                    DialogHandler.getInstance().show(ControlPanelActivity.this, "修改机型成功\n请重启电脑生效！");
                                } else {
                                    ToastHandler.getInstance().show(ControlPanelActivity.this, "未知错误！修改机型失败！", Toast.LENGTH_SHORT);
                                }
                            }
                        }.start();


                    }
                } catch (Exception e) {
                    Log.e("dev", "requirePermission:  error", e);
                    ToastHandler.getInstance().show(ControlPanelActivity.this, "修改机型失败！请检查网络和ROOT权限！", Toast.LENGTH_SHORT);

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

    private boolean checkBrand(String file1, String file2) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String var1 = readFile(file1);
        String var2 = readFile(file2);

        return !StringUtil.isEmpty(var1) && !StringUtil.isEmpty(var2) && var1.equals(var2);
    }

    private String readFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        BufferedReader reader = null;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            reader = new BufferedReader(new FileReader(file));
            String string;
            while ((string = reader.readLine()) != null) {
                stringBuilder.append(string);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return stringBuilder.toString();
    }

    private boolean pullFile(String deviceId) throws IOException {
        BufferedWriter fileWriter = null;
        BufferedWriter buildWriter = null;

        PassCheckRequest passCheckRequest = new PassCheckRequest(AppCache.account, AppCache.password, deviceId, AppConstant.INIT_SH);
        String param = JsonUtil.toJSONString(passCheckRequest);
        String content = OkHttpUtil.syncExeJsonPostGetString(AppConstant.HOST + AppConstant.PULL_PASS_CHECK_CONTENT_URI, param);
        System.out.println("init content:    " + content);
        PassCheckResponse response = JsonUtil.getObjectFromJson(content, PassCheckResponse.class);
        System.out.println("code:    " + response.getContent());
        if (!BaseResponse.isSuccess(response)) {
            ToastHandler.getInstance().show(ControlPanelActivity.this, "修改机型" + response.getErrorMessage(), Toast.LENGTH_LONG);
            return false;
        }

        File init = new File("/sdcard/init_pull.sh");
        if (!init.exists()) {
            init.createNewFile();
        }
        fileWriter = new BufferedWriter(new FileWriter(init));
        fileWriter.write(response.getContent());

        passCheckRequest = new PassCheckRequest(AppCache.account, AppCache.password, deviceId, AppConstant.BUILD_PROP);
        param = JsonUtil.toJSONString(passCheckRequest);
        content = OkHttpUtil.syncExeJsonPostGetString(AppConstant.HOST + AppConstant.PULL_PASS_CHECK_CONTENT_URI, param);
        System.out.println("build content:    " + content);
        response = JsonUtil.getObjectFromJson(content, PassCheckResponse.class);
        if (!BaseResponse.isSuccess(response)) {
            ToastHandler.getInstance().show(ControlPanelActivity.this, "修改机型" + response.getErrorMessage(), Toast.LENGTH_LONG);
            return false;
        }


        File build = new File("/sdcard/build_pull.prop");
        if (!build.exists()) {
            build.createNewFile();
        }
        buildWriter = new BufferedWriter(new FileWriter(build));
        buildWriter.write(response.getContent());

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
        return true;
    }

    private void stopPassCheck(final String codeKey) {

        new Thread() {
            @Override
            public void run() {
                DataOutputStream outputStream = null;
                try {
                    String deviceId = DeviceUniqueIdUtil.getDeviceId(ControlPanelActivity.this);
                    if (StringUtil.isEmpty(deviceId)) {
                        ToastHandler.getInstance().show(ControlPanelActivity.this, "关闭过检测失败，获取设备id出错！", Toast.LENGTH_SHORT);
                        return;
                    }

                    PassCheckRequest passCheckRequest = new PassCheckRequest(AppCache.account, AppCache.password, deviceId, codeKey);
                    String param = JsonUtil.toJSONString(passCheckRequest);
                    String content = OkHttpUtil.syncExeJsonPostGetString(AppConstant.HOST + AppConstant.PULL_PASS_CHECK_CONTENT_URI, param);
                    System.out.println("content:    " + content);
                    final PassCheckResponse response = JsonUtil.getObjectFromJson(content, PassCheckResponse.class);
                    System.out.println("code:    " + response.getContent());

                    if (BaseResponse.isSuccess(response)) {
                        Process exec = Runtime.getRuntime().exec("su\n");
                        BufferedReader br = new BufferedReader(new InputStreamReader(exec.getInputStream()));
                        outputStream = new DataOutputStream(exec.getOutputStream());
                        outputStream.writeBytes(response.getContent());
//                        new Run(br).start();
                        new Thread() {
                            @Override
                            public void run() {
                                if (checkCode(response.getContent())) {
                                    ToastHandler.getInstance().show(ControlPanelActivity.this, "关闭过检测成功！!", Toast.LENGTH_SHORT);
                                } else {
                                    ToastHandler.getInstance().show(ControlPanelActivity.this, "未知错误，关闭过检测失败！!", Toast.LENGTH_SHORT);
                                }
                            }
                        }.start();

                    } else {
                        ToastHandler.getInstance().show(ControlPanelActivity.this, "关闭过检测" + response.getErrorMessage(), Toast.LENGTH_LONG);
                    }

                } catch (Exception e) {
                    Log.e("dev", "stopPassCheck:  error", e);
                    ToastHandler.getInstance().show(ControlPanelActivity.this, "关闭过检测失败！请检查网络和ROOT权限！", Toast.LENGTH_SHORT);

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


    private void startPassCheck(final String codeKey) {
        new Thread() {
            @Override
            public void run() {
//                startPubg();
                DataOutputStream outputStream = null;
                try {

                    String deviceId = DeviceUniqueIdUtil.getDeviceId(ControlPanelActivity.this);
                    if (StringUtil.isEmpty(deviceId)) {
                        ToastHandler.getInstance().show(ControlPanelActivity.this, "开启过检测失败，获取设备id出错！", Toast.LENGTH_SHORT);
                        return;
                    }


                    PassCheckRequest passCheckRequest = new PassCheckRequest(AppCache.account, AppCache.password, deviceId, codeKey);
                    String param = JsonUtil.toJSONString(passCheckRequest);
                    String content = OkHttpUtil.syncExeJsonPostGetString(AppConstant.HOST + AppConstant.PULL_PASS_CHECK_CONTENT_URI, param);
                    System.out.println("content:    " + content);

                    final PassCheckResponse response = JsonUtil.getObjectFromJson(content, PassCheckResponse.class);
                    System.out.println("code:    " + response.getContent());

                    if (BaseResponse.isSuccess(response)) {
                        Process exec = Runtime.getRuntime().exec("su\n");
//                        BufferedReader br = new BufferedReader(new InputStreamReader(exec.getInputStream()));
//                        new Run(br).start();
                        outputStream = new DataOutputStream(exec.getOutputStream());
                        outputStream.writeBytes(response.getContent());
                        new Thread() {
                            @Override
                            public void run() {
                                if (AppConstant.PASS_CHECK2.equals(codeKey)) {
                                    ToastHandler.getInstance().show(ControlPanelActivity.this, "开启过检测成功！!", Toast.LENGTH_SHORT);
                                }else {
                                    if (checkCode(response.getContent())) {
                                        ToastHandler.getInstance().show(ControlPanelActivity.this, "开启过检测成功！!", Toast.LENGTH_SHORT);
                                    } else {
                                        ToastHandler.getInstance().show(ControlPanelActivity.this, "未知错误，开启过检测失败！!", Toast.LENGTH_SHORT);
                                    }
                                }
                            }
                        }.start();
                    } else {
                        ToastHandler.getInstance().show(ControlPanelActivity.this, "开启过检测" + response.getErrorMessage(), Toast.LENGTH_LONG);
                    }


                } catch (Exception e) {
                    Log.e("dev", "startPassCheck:  error", e);
                    ToastHandler.getInstance().show(ControlPanelActivity.this, "开启过检测失败！请检查网络和ROOT权限！", Toast.LENGTH_SHORT);
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

    private void startPubg() {
        PackageManager packageManager = getPackageManager();
        Intent launchIntentForPackage = packageManager.getLaunchIntentForPackage("com.tencent.tmgp.pubgmhd");
        if (launchIntentForPackage == null) {
            ToastHandler.getInstance().show(this, "请先安装游戏！", Toast.LENGTH_SHORT);
        } else {
            startActivity(launchIntentForPackage);
        }
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean checkCode(String content) {

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String[] string = content.split("\n");
        for (int i = 0; i < string.length; i++) {
            if (StringUtil.isEmpty(string[i])) {
                continue;
            }
            if (string[i].charAt(0) == 'r') {
                if (new File(string[i].split(" ")[2]).exists()) {
                    return false;
                }
            } else if (string[i].charAt(0) == 'm') {
                if (new File(string[i].split(" ")[1]).exists()) {
                    return false;
                }
            }
        }
        return true;

    }


    @Override
    protected void onResume() {
        showNotice();
        super.onResume();
    }

    private void showNotice() {
        notice.setText(AppCache.accountInfo);
    }

    public void onBackPressed() {

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
