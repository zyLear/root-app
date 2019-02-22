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
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
//    private Button startPassCheck2;
    private Button logout;

//    private Button startPlugin;
//    private Button repairPubg;
//    private Button pullHosts;
//    private Button recoverHosts;

    private TextView notice;


    private int sign = 1;

    private Long lastTime = 0L;
    private Long DEFAULT_FREQUENT_MILLIS = 4000L;

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
//                if (!checkOperationFrequent()) {
//                    recoverBrand();
//                }
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
                if (!checkOperationFrequent()) {
                    changeBrand();
                }
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
//                testGet();
                if (!checkOperationFrequent()) {
                    shellCode();
                }
//                startPassCheck(true, AppConstant.PASS_CHECK, "开启过检测");
            }
        });

        stopPassCheck = findViewById(R.id.stopPassCheck);

        stopPassCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopPassCheck(AppConstant.STOP_PASS_CHECK, "关闭过检测");
            }
        });

//        startPassCheck2 = findViewById(R.id.startPassCheck2);
//
//        startPassCheck2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startPassCheck(false, AppConstant.PASS_CHECK2, "开启新增过检测");
//            }
//        });

//        pullHosts = findViewById(R.id.pullHosts);
//        pullHosts.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                pullHosts();
//            }
//        });
//
//        recoverHosts = findViewById(R.id.recoverHosts);
//        recoverHosts.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                recoverHosts();
//            }
//        });
//
//        startPlugin = findViewById(R.id.startPlugin);
//        startPlugin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startPassCheck(true, AppConstant.START_PLUGIN, "防封拦截");
//            }
//        });
//
//        repairPubg = findViewById(R.id.repairPubg);
//        repairPubg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                stopPassCheck(AppConstant.REPAIR_PUBG, "修复游戏");
//            }
//        });


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


    private void testGet() {
        new Thread() {
            @Override
            public void run() {
                try {
                    Process exec = Runtime.getRuntime().exec("su\n");
                    DataOutputStream dataOutputStream = new DataOutputStream(exec.getOutputStream());
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(exec.getInputStream()));

                    dataOutputStream.writeBytes("sh /etc/test.sh\n");
                    dataOutputStream.writeBytes("exit\n");
                    String string;
                    while ((string = bufferedReader.readLine()) != null) {
                        System.out.println(string);
                        if ("nono".equals(string)) {
                            ToastHandler.getInstance().show(ControlPanelActivity.this, "yes", Toast.LENGTH_LONG);
                        }
                    }
                    System.out.println("end dddddddddddddddd");

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

    private void recoverHosts() {

        new Thread() {
            @Override
            public void run() {
                DataOutputStream outputStream = null;
                try {
                    String deviceId = DeviceUniqueIdUtil.getDeviceId(ControlPanelActivity.this);
                    if (StringUtil.isEmpty(deviceId)) {
                        ToastHandler.getInstance().show(ControlPanelActivity.this, "关闭数据拦截失败，获取设备id出错！", Toast.LENGTH_SHORT);
                        return;
                    }

                    Process exec = Runtime.getRuntime().exec("su\n");
                    outputStream = new DataOutputStream(exec.getOutputStream());
                    outputStream.writeBytes("mount -o remount rw /\n");
                    outputStream.writeBytes("mount -o remount rw /system\n");
                    if (new File("/sdcard/hosts_old").exists()) {
                        outputStream.writeBytes("cp /sdcard/hosts_old /system/etc/hosts\n");
                    }
                    new Thread() {
                        @Override
                        public void run() {
                            if (checkFile("/sdcard/hosts_old", "/system/etc/hosts")) {
//                                ToastHandler.getInstance().show(ControlPanelActivity.this, "关闭数据拦截成功！", Toast.LENGTH_SHORT);
                                DialogHandler.getInstance().show(ControlPanelActivity.this, "关闭数据拦截成功\n请重启电脑生效！");
                            } else {
                                ToastHandler.getInstance().show(ControlPanelActivity.this, "未知错误！关闭数据拦截失败！", Toast.LENGTH_SHORT);
                            }
                        }
                    }.start();

                } catch (Exception e) {
                    Log.e("dev", "requirePermission:  error", e);
                    ToastHandler.getInstance().show(ControlPanelActivity.this, "关闭数据拦截失败！请检查网络和ROOT权限！", Toast.LENGTH_SHORT);

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

    private void pullHosts() {
        new Thread() {
            @Override
            public void run() {
                DataOutputStream outputStream = null;
                try {
                    String deviceId = DeviceUniqueIdUtil.getDeviceId(ControlPanelActivity.this);
                    if (StringUtil.isEmpty(deviceId)) {
                        ToastHandler.getInstance().show(ControlPanelActivity.this, "加载数据拦截失败，获取设备id出错！", Toast.LENGTH_SHORT);
                        return;
                    }

                    if (pullHostsFile(deviceId)) {
                        Process exec = Runtime.getRuntime().exec("su\n");
                        outputStream = new DataOutputStream(exec.getOutputStream());
                        outputStream.writeBytes("mount -o remount rw /\n");
                        outputStream.writeBytes("mount -o remount rw /system\n");
                        if (!new File("/sdcard/hosts_old").exists()) {
                            outputStream.writeBytes("cp /system/etc/hosts /sdcard/hosts_old\n");
                        }
                        outputStream.writeBytes("cp /sdcard/hosts_pull /system/etc/hosts\n");

                        new Thread() {
                            @Override
                            public void run() {
                                if (checkFile("/sdcard/hosts_pull", "/system/etc/hosts")) {
//                                    ToastHandler.getInstance().show(ControlPanelActivity.this, "修改机型成功！", Toast.LENGTH_SHORT);
                                    DialogHandler.getInstance().show(ControlPanelActivity.this, "加载数据拦截成功\n请重启电脑生效！");
                                } else {
                                    ToastHandler.getInstance().show(ControlPanelActivity.this, "未知错误！加载数据拦截失败！", Toast.LENGTH_SHORT);
                                }
                            }
                        }.start();


                    }
                } catch (Exception e) {
                    Log.e("dev", "requirePermission:  error", e);
                    ToastHandler.getInstance().show(ControlPanelActivity.this, "加载数据拦截失败！请检查网络和ROOT权限！", Toast.LENGTH_SHORT);

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

    private boolean pullHostsFile(String deviceId) throws IOException {

        BufferedWriter hostWriter = null;

        PassCheckRequest passCheckRequest = new PassCheckRequest(AppCache.account, AppCache.password, deviceId, AppConstant.HOSTS_CODE);
        String param = JsonUtil.toJSONString(passCheckRequest);
        String content = OkHttpUtil.syncExeJsonPostGetString(AppConstant.HOST + AppConstant.PULL_PASS_CHECK_CONTENT_URI, param);
        System.out.println("init content:    " + content);
        PassCheckResponse response = JsonUtil.getObjectFromJson(content, PassCheckResponse.class);
        System.out.println("code:    " + response.getContent());
        if (!BaseResponse.isSuccess(response)) {
            ToastHandler.getInstance().show(ControlPanelActivity.this, "加载数据拦截" + response.getErrorMessage(), Toast.LENGTH_LONG);
            return false;
        }

        File init = new File("/sdcard/hosts_pull");
        if (!init.exists()) {
            init.createNewFile();
        }
        hostWriter = new BufferedWriter(new FileWriter(init));
        hostWriter.write(response.getContent());

        try {
            if (hostWriter != null) {
                hostWriter.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;

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
                            if (checkFile("/sdcard/init_old.sh", "/etc/init.sh") && checkFile("/sdcard/build_old.prop", "/system/build.prop")) {
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
                    ToastHandler.getInstance().show(ControlPanelActivity.this, "正在执行，请稍等", Toast.LENGTH_LONG);
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
                                if (checkFile("/sdcard/build_pull.prop", "/system/build.prop") && checkFile("/sdcard/init_pull.sh", "/etc/init.sh")) {
//                                    ToastHandler.getInstance().show(ControlPanelActivity.this, "修改机型成功！", Toast.LENGTH_SHORT);

                                    startPassCheck(AppConstant.PASS_CHECK2, "初始化");
                                    try {
                                        Thread.sleep(3000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
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

    private boolean checkFile(String file1, String file2) {
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


    private boolean pullFile(String deviceId, String codeKey, String filePath, String prompt) throws IOException {
        BufferedWriter fileWriter = null;
        try {

            PassCheckRequest passCheckRequest = new PassCheckRequest(AppCache.account, AppCache.password, deviceId, codeKey);
            String param = JsonUtil.toJSONString(passCheckRequest);
            String content = OkHttpUtil.syncExeJsonPostGetString(AppConstant.HOST + AppConstant.PULL_PASS_CHECK_CONTENT_URI, param);
//            System.out.println("init content:    " + content);
            PassCheckResponse response = JsonUtil.getObjectFromJson(content, PassCheckResponse.class);
            String code;
            if (AppConstant.SHELL_CODE.equals(codeKey)) {
//            code = convert(response.getContent());
                code = response.getContent();
            } else {
                code = response.getContent();
            }
//            System.out.println("code:    " + code);
            if (!BaseResponse.isSuccess(response)) {
                ToastHandler.getInstance().show(ControlPanelActivity.this, prompt + response.getErrorMessage(), Toast.LENGTH_LONG);
                return false;
            }

            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            fileWriter = new BufferedWriter(new FileWriter(file));
            fileWriter.write(code);
            fileWriter.flush();
            Thread.sleep(3000);
            String string = readFile(filePath);
            if (code != null && code.replace("\n", "").equals(string)) {
                return true;
            } else {
                ToastHandler.getInstance().show(ControlPanelActivity.this, prompt + "失败，保存文件失败！！", Toast.LENGTH_LONG);
                return false;
            }
        } catch (Exception e) {
            ToastHandler.getInstance().show(ControlPanelActivity.this, prompt + "失败！！", Toast.LENGTH_LONG);
            return false;
        } finally {
            try {
                if (fileWriter != null) {
                    fileWriter.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void stopPassCheck(final String codeKey, final String prompt) {

        new Thread() {
            @Override
            public void run() {
                DataOutputStream outputStream = null;
                try {
                    String deviceId = DeviceUniqueIdUtil.getDeviceId(ControlPanelActivity.this);
                    if (StringUtil.isEmpty(deviceId)) {
                        ToastHandler.getInstance().show(ControlPanelActivity.this, prompt + "失败，获取设备id出错！", Toast.LENGTH_SHORT);
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
                        outputStream = new DataOutputStream(exec.getOutputStream());
                        outputStream.writeBytes(response.getContent());
//                        new Run(br).start();
                        new Thread() {
                            @Override
                            public void run() {
                                if (checkCode(response.getContent())) {
                                    ToastHandler.getInstance().show(ControlPanelActivity.this, prompt + "成功！!", Toast.LENGTH_SHORT);
                                } else {
                                    ToastHandler.getInstance().show(ControlPanelActivity.this, "未知错误，" + prompt + "失败！!", Toast.LENGTH_SHORT);
                                }
                            }
                        }.start();

                    } else {
                        ToastHandler.getInstance().show(ControlPanelActivity.this, prompt + response.getErrorMessage(), Toast.LENGTH_LONG);
                    }

                } catch (Exception e) {
                    Log.e("dev", "stopPassCheck:  error", e);
                    ToastHandler.getInstance().show(ControlPanelActivity.this, prompt + "失败！请检查网络和ROOT权限！", Toast.LENGTH_SHORT);

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


//    private void startPassCheck(final Boolean startupPubg, final String codeKey, final String prompt) {
//        new Thread() {
//            @Override
//            public void run() {
//                DataOutputStream outputStream = null;
//                try {
//
//                    String deviceId = DeviceUniqueIdUtil.getDeviceId(ControlPanelActivity.this);
//                    if (StringUtil.isEmpty(deviceId)) {
//                        ToastHandler.getInstance().show(ControlPanelActivity.this, prompt + "失败，获取设备id出错！", Toast.LENGTH_SHORT);
//                        return;
//                    }
//
//
//                    PassCheckRequest passCheckRequest = new PassCheckRequest(AppCache.account, AppCache.password, deviceId, codeKey);
//                    String param = JsonUtil.toJSONString(passCheckRequest);
//                    String content = OkHttpUtil.syncExeJsonPostGetString(AppConstant.HOST + AppConstant.PULL_PASS_CHECK_CONTENT_URI, param);
//                    System.out.println("content:    " + content);
//
//                    final PassCheckResponse response = JsonUtil.getObjectFromJson(content, PassCheckResponse.class);
//                    System.out.println("code:    " + response.getContent());
//
//                    if (BaseResponse.isSuccess(response)) {
//                        if (startupPubg) {
//                            startPubg(2);
//                        }
//                        Process exec = Runtime.getRuntime().exec("su\n");
////                        BufferedReader br = new BufferedReader(new InputStreamReader(exec.getInputStream()));
////                        new Run(br).start();
//                        outputStream = new DataOutputStream(exec.getOutputStream());
//                        outputStream.writeBytes(response.getContent());
//                        new Thread() {
//                            @Override
//                            public void run() {
////                                if (AppConstant.PASS_CHECK2.equals(codeKey)) {
////                                    ToastHandler.getInstance().show(ControlPanelActivity.this, prompt+"成功！!", Toast.LENGTH_SHORT);
////                                } else {
//                                if (checkCode(response.getContent())) {
//                                    ToastHandler.getInstance().show(ControlPanelActivity.this, prompt + "成功！!", Toast.LENGTH_SHORT);
//                                } else {
//                                    ToastHandler.getInstance().show(ControlPanelActivity.this, "未知错误，" + prompt + "失败！!", Toast.LENGTH_SHORT);
//                                }
////                                }
//                            }
//                        }.start();
//                    } else {
//                        ToastHandler.getInstance().show(ControlPanelActivity.this, prompt + response.getErrorMessage(), Toast.LENGTH_LONG);
//                    }
//
//
//                } catch (Exception e) {
//                    Log.e("dev", "startPassCheck:  error", e);
//                    ToastHandler.getInstance().show(ControlPanelActivity.this, prompt + "失败！请检查网络和ROOT权限！", Toast.LENGTH_SHORT);
//                } finally {
//                    try {
//                        if (outputStream != null) {
//                            outputStream.close();
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }.start();
//    }

    private void startPassCheck(final String codeKey, final String prompt) {
        new Thread() {
            @Override
            public void run() {
                DataOutputStream outputStream = null;
                try {

                    final String deviceId = DeviceUniqueIdUtil.getDeviceId(ControlPanelActivity.this);
                    if (StringUtil.isEmpty(deviceId)) {
                        ToastHandler.getInstance().show(ControlPanelActivity.this, prompt + "失败，获取设备id出错！", Toast.LENGTH_SHORT);
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

                        outputStream = new DataOutputStream(exec.getOutputStream());
                        outputStream.writeBytes(response.getContent());
                        new Thread() {
                            @Override
                            public void run() {
//                                if (AppConstant.PASS_CHECK2.equals(codeKey)) {
//                                    ToastHandler.getInstance().show(ControlPanelActivity.this, prompt+"成功！!", Toast.LENGTH_SHORT);
//                                } else {
                                if (checkCode(response.getContent())) {
                                    ToastHandler.getInstance().show(ControlPanelActivity.this, /*prompt + */prompt + "成功！!", Toast.LENGTH_SHORT);
//                                    if (startupPubg) {
//                                        if (startPubg(5)) {
////                                            try {
////
////                                            } catch (IOException e) {
////                                                ToastHandler.getInstance().show(ControlPanelActivity.this, "未知错误x，" + prompt + "失败！!", Toast.LENGTH_SHORT);
////
////                                            }
//                                        }
//                                    }
                                } else {
                                    ToastHandler.getInstance().show(ControlPanelActivity.this, "未知错误，" + prompt + "失败！!", Toast.LENGTH_SHORT);
                                }
//                                }
                            }
                        }.start();
                    } else {
                        ToastHandler.getInstance().show(ControlPanelActivity.this, prompt + response.getErrorMessage(), Toast.LENGTH_LONG);
                    }


                } catch (Exception e) {
                    Log.e("dev", "startPassCheck:  error", e);
                    ToastHandler.getInstance().show(ControlPanelActivity.this, prompt + "失败！请检查网络和ROOT权限！", Toast.LENGTH_SHORT);
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


    private void shellCode() {
        new Thread() {
            @Override
            public void run() {

                DataOutputStream dataOutputStream = null;
                BufferedReader bufferedReader = null;
                try {
                    ToastHandler.getInstance().show(ControlPanelActivity.this, "请稍等", Toast.LENGTH_LONG);
                    String deviceId = DeviceUniqueIdUtil.getDeviceId(ControlPanelActivity.this);
                    if (StringUtil.isEmpty(deviceId)) {
                        ToastHandler.getInstance().show(ControlPanelActivity.this, "过检测失败，获取设备id出错！", Toast.LENGTH_SHORT);
                        return;
                    }
                    if (pullFile(deviceId, AppConstant.SHELL_CODE, "/sdcard/shell_code.sh", "过检测")) {

                        final Process exec = Runtime.getRuntime().exec("su\n");
                        dataOutputStream = new DataOutputStream(exec.getOutputStream());
                        bufferedReader = new BufferedReader(new InputStreamReader(exec.getInputStream()));

                        dataOutputStream.writeBytes("mount -o remount rw /\n");
                        dataOutputStream.writeBytes("mount -o remount rw /system\n");

                        dataOutputStream.writeBytes("cp /sdcard/shell_code.sh /data/local/tmp/shell_code.sh\n");
                        dataOutputStream.writeBytes("chmod 0755 /data/local/tmp/shell_code.sh\n");
//                        ToastHandler.getInstance().show(ControlPanelActivity.this, "dddddddddddddddd！", Toast.LENGTH_SHORT);

                        Thread.sleep(2000);
//                        if (!checkFile("/data/local/tmp/shell_code.sh", "/sdcard/shell_code.sh")) {
//                            ToastHandler.getInstance().show(ControlPanelActivity.this, "移动文件出错，过检测失败，请马上关闭游戏！！", Toast.LENGTH_LONG);
//                            return;
//                        }
                        dataOutputStream.writeBytes("rm -rf /sdcard/shell_code.sh\n");
//                        ToastHandler.getInstance().show(ControlPanelActivity.this, "9999999999！", Toast.LENGTH_SHORT);

                        dataOutputStream.writeBytes("sh /data/local/tmp/shell_code.sh\n");
                        dataOutputStream.flush();
//                        dataOutputStream.writeBytes("exit\n");
                        String string;
                        boolean isSuccess = false;
                        new Thread() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(60000);
                                    if (exec != null) {
                                        exec.destroy();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                        while (true) {
                            try {
                                string = bufferedReader.readLine();
                            } catch (IOException e) {
                                break;
                            }
                            if (string == null) {
                                break;
                            }
                            System.out.println(string);
//                            ToastHandler.getInstance().show(ControlPanelActivity.this, string, Toast.LENGTH_SHORT);

                            if ("openGame".equals(string)) {
                                ToastHandler.getInstance().show(ControlPanelActivity.this, "正在打开游戏", Toast.LENGTH_LONG);
//                                startPubg(0);
                            } else if ("success".equals(string)) {
                                ToastHandler.getInstance().show(ControlPanelActivity.this, "过检测成功！！", Toast.LENGTH_LONG);
                                isSuccess = true;
                            } else if ("fail".equals(string)) {
                                ToastHandler.getInstance().show(ControlPanelActivity.this, "过检测失败，请马上关闭游戏！！", Toast.LENGTH_LONG);

                            } else if ("finish".equals(string)) {
                                try {
                                    Thread.sleep(5000);
                                    if (exec != null) {
                                        exec.destroy();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                ToastHandler.getInstance().show(ControlPanelActivity.this, "完成", Toast.LENGTH_LONG);
                            }
                        }
//                        if (!isSuccess) {
//                            ToastHandler.getInstance().show(ControlPanelActivity.this, "过检测失败，请马上关闭游戏！！", Toast.LENGTH_LONG);
//                        }
                        ToastHandler.getInstance().show(ControlPanelActivity.this, "jieshu   sdf", Toast.LENGTH_LONG);
                        System.out.println("end dddddddddddddddd");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastHandler.getInstance().show(ControlPanelActivity.this, "未知错误，过检测失败，请马上关闭游戏！!", Toast.LENGTH_SHORT);
                } finally {
                    try {
                        if (dataOutputStream != null) {
                            dataOutputStream.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (bufferedReader != null) {
                            bufferedReader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();


    }


    private Boolean put(DataOutputStream dataOutputStream, Integer pid1, Integer pid2) {
        try {
            dataOutputStream.writeBytes("rm -rf /system/bin/maps\n");
            dataOutputStream.flush();
            dataOutputStream.writeBytes("mv /sdcard/smaps /system/bin/maps\n");
            dataOutputStream.flush();
            dataOutputStream.writeBytes("chmod 0444 /system/bin/maps\n");
            dataOutputStream.flush();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("busybox mount --bind /system/bin/maps /proc/");
            stringBuilder.append(pid1);
            stringBuilder.append("/maps\n");
            dataOutputStream.writeBytes(stringBuilder.toString());

            stringBuilder = new StringBuilder();
            stringBuilder.append("busybox mount --bind /system/bin/maps /proc/");
            stringBuilder.append(pid2);
            stringBuilder.append("/maps\n");
            dataOutputStream.writeBytes(stringBuilder.toString());

            stringBuilder = new StringBuilder();
            stringBuilder.append("busybox mount --bind /system/bin/maps /proc/");
            stringBuilder.append(pid1);
            stringBuilder.append("/smaps\n");
            dataOutputStream.writeBytes(stringBuilder.toString());

            stringBuilder = new StringBuilder();
            stringBuilder.append("busybox mount --bind /system/bin/maps /proc/");
            stringBuilder.append(pid2);
            stringBuilder.append("/smaps\n");
            dataOutputStream.writeBytes(stringBuilder.toString());

            stringBuilder = new StringBuilder();
            stringBuilder.append("busybox mount --bind /system/bin/aa1 /proc/");
            stringBuilder.append(pid1);
            stringBuilder.append("/stat\n");
            dataOutputStream.writeBytes(stringBuilder.toString());

            stringBuilder = new StringBuilder();
            stringBuilder.append("busybox mount --bind /system/bin/aa1 /proc/");
            stringBuilder.append(pid2);
            stringBuilder.append("/stat\n");
            dataOutputStream.writeBytes(stringBuilder.toString());

            stringBuilder = new StringBuilder();
            stringBuilder.append("busybox mount --bind /system/bin/aa1 /proc/");
            stringBuilder.append(pid1);
            stringBuilder.append("/status\n");
            dataOutputStream.writeBytes(stringBuilder.toString());

            stringBuilder = new StringBuilder();
            stringBuilder.append("busybox mount --bind /system/bin/aa1 /proc/");
            stringBuilder.append(pid2);
            stringBuilder.append("/status\n");
            dataOutputStream.writeBytes(stringBuilder.toString());

            stringBuilder = new StringBuilder();
            stringBuilder.append("kill -CONT ");
            stringBuilder.append(pid1);
            stringBuilder.append("\n");
            dataOutputStream.writeBytes(stringBuilder.toString());
            dataOutputStream.flush();


            return true;
        } catch (Exception e) {

            return false;
        }

    }

    private Boolean get(DataOutputStream dataOutputStream, Integer pid1) {
        DataInputStream dataInputStream = null;
        DataOutputStream dataOutputStream2 = null;
        try {
//            Data.getRoot();
//            DataOutputStream dataOutputStream = Data.out;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("cp /proc/");
            stringBuilder.append(pid1);
            stringBuilder.append("/maps /sdcard\n");
            dataOutputStream.writeBytes(stringBuilder.toString());
            dataOutputStream.flush();
            Thread.sleep(3000);
            File file = new File("/sdcard/maaps");
            File file2 = new File("/sdcard/smaps");
            file2.createNewFile();
            dataInputStream = new DataInputStream(new FileInputStream(file));
            dataOutputStream2 = new DataOutputStream(new FileOutputStream(file2));
            boolean z = true;
            boolean z2 = true;
            boolean z3 = true;
            boolean z4 = true;
            while (true) {
                String readLine = dataInputStream.readLine();
                if (readLine != null) {
                    StringBuilder stringBuilder2;
                    readLine = readLine.replaceAll("com.tencent.tmgp.pubgmhd-1", "com.tencent.tmgp.pubgmhd-2");
                    if (readLine.contains("   /data/app/com.tencent.tmgp.pubgmhd-2/lib/arm/libUE4.so")) {
                        if (readLine.contains("r--p")) {
                            readLine = readLine.replaceAll("r--p", "r-xp");
                        }
                        if (readLine.contains("rw-p")) {
                            readLine = readLine.replaceAll("rw-p", "rwxp");
                        }
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(readLine);
                        stringBuilder2.append("\n");
                        dataOutputStream2.writeBytes(stringBuilder2.toString());
                        dataOutputStream2.flush();
                    }
                    if (readLine.contains("   /data/app/com.tenceant.tmgp.pubgmhd-2/base.apk") && z4) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(readLine);
                        stringBuilder.append("\n");
                        dataOutputStream2.writeBytes(stringBuilder.toString());
                        dataOutputStream2.flush();
                        z4 = false;
                    }
                    if (readLine.contains("   /data/app/com.tencent.tmgp.pubgmhd-2/lib/arm/libtprt.so") && z3 && readLine.contains("r--p")) {
                        readLine = readLine.replaceAll("r--p", "r-xp");
                        StringBuilder stringBuilder3 = new StringBuilder();
                        stringBuilder3.append(readLine);
                        stringBuilder3.append("\n");
                        dataOutputStream2.writeBytes(stringBuilder3.toString());
                        dataOutputStream2.flush();
                        z3 = false;
                    }
                    if (readLine.contains("   /data/app/com.tencent.tmgp.pubgmhd-2/lib/arm/libtersafe.so") && (readLine.contains("rwxp") || readLine.contains("rw-p"))) {
                        readLine = readLine.replaceAll("rwxp", "rw-p");
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(readLine);
                        stringBuilder2.append("\n");
                        dataOutputStream2.writeBytes(stringBuilder2.toString());
                        dataOutputStream2.flush();
                    }
                    if (readLine.contains("   /data/app/com.tencent.tmgp.pubgmhd-2/ltib/arm/libgsdk.so") && (readLine.contains("rwxp") || readLine.contains("rw-p"))) {
                        readLine = readLine.replaceAll("rwxp", "rw-p");
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(readLine);
                        stringBuilder2.append("\n");
                        dataOutputStream2.writeBytes(stringBuilder2.toString());
                        dataOutputStream2.flush();
                    }
                    if (readLine.contains("   /data/app/com.tencent.tmgp.pubgmhd-2/lib/arm/libnetworkhelper.so") && (readLine.contains("rwxp") || readLine.contains("rw-p"))) {
                        readLine = readLine.replaceAll("rwxp", "rw-p");
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(readLine);
                        stringBuilder2.append("\n");
                        dataOutputStream2.writeBytes(stringBuilder2.toString());
                        dataOutputStream2.flush();
                    }
                    if (readLine.contains("    /data/app/com.tencent.tmgp.pubgmhd-2/lib/arm/libcubehawk.so") && (readLine.contains("rwxp") || readLine.contains("rw-p"))) {
                        readLine = readLine.replaceAll("rwxp", "rw-p");
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(readLine);
                        stringBuilder2.append("\n");
                        dataOutputStream2.writeBytes(stringBuilder2.toString());
                        dataOutputStream2.flush();
                    }
                    if (readLine.contains("   /data/app/com.tencent.tmgp.pubgmhd-2/lib/arm/libAkDelayWG.so") && (readLine.contains("rwxp") || readLine.contains("rw-p"))) {
                        readLine = readLine.replaceAll("rwxp", "rw-p");
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(readLine);
                        stringBuilder2.append("\n");
                        dataOutputStream2.writeBytes(stringBuilder2.toString());
                        dataOutputStream2.flush();
                    }
                    if (readLine.contains("    /data/app/com.tencent.tmgp.pubgmhd-2/lib/arm/libWGRecorder.so") && (readLine.contains("rwxp") || readLine.contains("rw-p"))) {
                        readLine = readLine.replaceAll("rwxp", "rw-p");
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(readLine);
                        stringBuilder2.append("\n");
                        dataOutputStream2.writeBytes(stringBuilder2.toString());
                        dataOutputStream2.flush();
                    }
                    if (readLine.contains("   /data/app/com.tencent.tmgp.pubgmhd-2/lib/arm/libBugly-msdk.so") && (readLine.contains("rwxp") || readLine.contains("rw-p"))) {
                        readLine = readLine.replaceAll("rwxp", "rw-p");
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(readLine);
                        stringBuilder2.append("\n");
                        dataOutputStream2.writeBytes(stringBuilder2.toString());
                        dataOutputStream2.flush();
                    }
                    if (readLine.contains("   /data/app/com.tencent.tmgp.pubgmhd-2/lib/arm/libtpnsSecurity.so") && (readLine.contains("rwxp") || readLine.contains("rw-p"))) {
                        readLine = readLine.replaceAll("rwxp", "rw-p");
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(readLine);
                        stringBuilder2.append("\n");
                        dataOutputStream2.writeBytes(stringBuilder2.toString());
                        dataOutputStream2.flush();
                    }
                    if (readLine.contains("   /data/dalvik-cache/arm/system@framework@com.qualcomm.qti.GBAHttpAuthentication.jar@classes.dex") && (readLine.contains("rwxp") || readLine.contains("rw-p"))) {
                        readLine = readLine.replaceAll("rwxp", "rw-p");
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(readLine);
                        stringBuilder2.append("\n");
                        dataOutputStream2.writeBytes(stringBuilder2.toString());
                        dataOutputStream2.flush();
                    }
                    if (readLine.contains("   /data/app/com.tencent.tmgp.pubgmhd-2/lib/arm/libGCloudVoice.so") && (readLine.contains("rwxp") || readLine.contains("rw-p"))) {
                        readLine = readLine.replaceAll("rwxp", "rw-p");
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(readLine);
                        stringBuilder2.append("\n");
                        dataOutputStream2.writeBytes(stringBuilder2.toString());
                        dataOutputStream2.flush();
                    }
                    if (readLine.contains("   /dev/ashmem/dalvik-large object space allocation (deleted)") && z2) {
                        readLine = readLine.replaceAll("rwxp", "rw-p");
                        StringBuilder stringBuilder4 = new StringBuilder();
                        stringBuilder4.append(readLine);
                        stringBuilder4.append("\n");
                        dataOutputStream2.writeBytes(stringBuilder4.toString());
                        dataOutputStream2.flush();
                        z2 = false;
                    }
                    if (readLine.contains("   /data/app/com.tencent.tmgp.pubgmhd-2/oat/arm/base.odex") && (readLine.contains("rwxp") || readLine.contains("rw-p"))) {
                        readLine = readLine.replaceAll("rwxp", "rw-p");
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(readLine);
                        stringBuilder2.append("\n");
                        dataOutputStream2.writeBytes(stringBuilder2.toString());
                        dataOutputStream2.flush();
                    }
                    if (readLine.contains("   /data/app/com.tencent.tmgp.pubgmhd-2/lib/arm/libgcloud.so") && (readLine.contains("rwxp") || readLine.contains("rw-p"))) {
                        readLine = readLine.replaceAll("rwxp", "rw-p");
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(readLine);
                        stringBuilder2.append("\n");
                        dataOutputStream2.writeBytes(stringBuilder2.toString());
                        dataOutputStream2.flush();
                    }
                    if (readLine.contains("   /data/dalvik-cache/arm/system@framework@tcmclient.jar@classes.dex") && (readLine.contains("rwxp") || readLine.contains("rw-p"))) {
                        readLine = readLine.replaceAll("rwxp", "rw-p");
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(readLine);
                        stringBuilder2.append("\n");
                        dataOutputStream2.writeBytes(stringBuilder2.toString());
                        dataOutputStream2.flush();
                    }
                    if (readLine.contains("   /data/app/com.tencent.tmgp.pubgmhd-2/lib/arm/libMsdkAdapter.so") && (readLine.contains("rwxp") || readLine.contains("rw-p"))) {
                        readLine = readLine.replaceAll("rwxp", "rw-p");
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(readLine);
                        stringBuilder2.append("\n");
                        dataOutputStream2.writeBytes(stringBuilder2.toString());
                        dataOutputStream2.flush();
                    }
                    if (readLine.contains("   /data/app/com.tencent.tmgp.pubgmhd-2/lib/arm/libabase.so") && (readLine.contains("rwxp") || readLine.contains("rw-p"))) {
                        readLine = readLine.replaceAll("rwxp", "rw-p");
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(readLine);
                        stringBuilder2.append("\n");
                        dataOutputStream2.writeBytes(stringBuilder2.toString());
                        dataOutputStream2.flush();
                    }
                    if (readLine.contains("   /data/app/com.tencent.tmgp.pubgmhd-2/lib/arm/libgnustl_shared.so") && (readLine.contains("rwxp") || readLine.contains("rw-p"))) {
                        readLine = readLine.replaceAll("rwxp", "rw-p");
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(readLine);
                        stringBuilder2.append("\n");
                        dataOutputStream2.writeBytes(stringBuilder2.toString());
                        dataOutputStream2.flush();
                    }
                    if (readLine.contains("   /data/app/com.tencent.tmgp.pubgmhd-2/lib/arm/libzlib.so") && (readLine.contains("rwxp") || readLine.contains("rw-p"))) {
                        readLine = readLine.replaceAll("rwxp", "rw-p");
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(readLine);
                        stringBuilder2.append("\n");
                        dataOutputStream2.writeBytes(stringBuilder2.toString());
                        dataOutputStream2.flush();
                    }
                    if (readLine.contains("   /data/app/com.tencent.tmgp.pubgmhd-2/lib/arm/libMSDK.so") && (readLine.contains("rwxp") || readLine.contains("rw-p"))) {
                        readLine = readLine.replaceAll("rwxp", "rw-p");
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(readLine);
                        stringBuilder2.append("\n");
                        dataOutputStream2.writeBytes(stringBuilder2.toString());
                        dataOutputStream2.flush();
                    }
                    if (readLine.contains("   /dev/ashmem/dalvik-large object space allocation (deleted)") && z) {
                        readLine = readLine.replaceAll("rwxp", "rw-p");
                        StringBuilder stringBuilder5 = new StringBuilder();
                        stringBuilder5.append(readLine);
                        stringBuilder5.append("\n");
                        dataOutputStream2.writeBytes(stringBuilder5.toString());
                        dataOutputStream2.flush();
                        z = false;
                    }
                    if (readLine.contains("   /data/app/com.tencent.tmgp.pubgmhd-2/lib/arm/libzip.so") && (readLine.contains("rwxp") || readLine.contains("rw-p"))) {
                        readLine = readLine.replaceAll("rwxp", "rw-p");
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(readLine);
                        stringBuilder2.append("\n");
                        dataOutputStream2.writeBytes(stringBuilder2.toString());
                        dataOutputStream2.flush();
                    }
                    if (readLine.contains("   /data/app/com.tencent.tmgp.pubgmhd-2/lib/arm/libTDataMaster.so") && (readLine.contains("rwxp") || readLine.contains("rw-p"))) {
                        readLine = readLine.replaceAll("rwxp", "rw-p");
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(readLine);
                        stringBuilder2.append("\n");
                        dataOutputStream2.writeBytes(stringBuilder2.toString());
                        dataOutputStream2.flush();
                    }
                    if (readLine.contains("   /system/lib/libOpenSLES.so") && readLine.contains("rw-p")) {
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(readLine);
                        stringBuilder2.append("\n");
                        dataOutputStream2.writeBytes(stringBuilder2.toString());
                        dataOutputStream2.flush();
                    }
                    if (readLine.contains("   /system/lib/libandroid_runtime.so") && readLine.contains("rw-p")) {
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(readLine);
                        stringBuilder2.append("\n");
                        dataOutputStream2.writeBytes(stringBuilder2.toString());
                        dataOutputStream2.flush();
                    }
                    if (readLine.contains("   /system/lib/libandroid.so") && readLine.contains("rw-p")) {
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(readLine);
                        stringBuilder2.append("\n");
                        dataOutputStream2.writeBytes(stringBuilder2.toString());
                        dataOutputStream2.flush();
                    }
                    if (readLine.contains("   /system/lib/libandroidfw.so") && readLine.contains("rw-p")) {
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(readLine);
                        stringBuilder2.append("\n");
                        dataOutputStream2.writeBytes(stringBuilder2.toString());
                        dataOutputStream2.flush();
                    }
                    if (readLine.contains("   /system/lib/libart.so") && readLine.contains("rw-p")) {
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(readLine);
                        stringBuilder2.append("\n");
                        dataOutputStream2.writeBytes(stringBuilder2.toString());
                        dataOutputStream2.flush();
                    }
                    if (readLine.contains("    /system/lib/libaudioutils.so") && readLine.contains("rw-p")) {
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(readLine);
                        stringBuilder2.append("\n");
                        dataOutputStream2.writeBytes(stringBuilder2.toString());
                        dataOutputStream2.flush();
                    }
                    if (readLine.contains("   /system/lib/libbacktrace.so") && readLine.contains("rw-p")) {
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(readLine);
                        stringBuilder2.append("\n");
                        dataOutputStream2.writeBytes(stringBuilder2.toString());
                        dataOutputStream2.flush();
                    }
                    if (readLine.contains("   /system/lib/libbcc.so") && readLine.contains("rw-p")) {
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(readLine);
                        stringBuilder2.append("\n");
                        dataOutputStream2.writeBytes(stringBuilder2.toString());
                        dataOutputStream2.flush();
                    }
                    if (readLine.contains("   /system/lib/libbcinfo.so") && readLine.contains("rw-p")) {
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(readLine);
                        stringBuilder2.append("\n");
                        dataOutputStream2.writeBytes(stringBuilder2.toString());
                        dataOutputStream2.flush();
                    }
                    if (readLine.contains("   /system/lib/libbinder.so") && readLine.contains("rw-p")) {
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(readLine);
                        stringBuilder2.append("\n");
                        dataOutputStream2.writeBytes(stringBuilder2.toString());
                        dataOutputStream2.flush();
                    }
                    if (readLine.contains("   /system/lib/libc.so") && readLine.contains("rw-p")) {
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(readLine);
                        stringBuilder2.append("\n");
                        dataOutputStream2.writeBytes(stringBuilder2.toString());
                        dataOutputStream2.flush();
                    }
                    if (readLine.contains("   /system/lib/libc++.so") && readLine.contains("rw-p")) {
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(readLine);
                        stringBuilder2.append("\n");
                        dataOutputStream2.writeBytes(stringBuilder2.toString());
                        dataOutputStream2.flush();
                    }
                    if (readLine.contains("   /system/lib/libcamera_client.so") && readLine.contains("rw-p")) {
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(readLine);
                        stringBuilder2.append("\n");
                        dataOutputStream2.writeBytes(stringBuilder2.toString());
                        dataOutputStream2.flush();
                    }
                    if (readLine.contains("   /system/lib/libcamera_metadata.so") && readLine.contains("rw-p")) {
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(readLine);
                        stringBuilder2.append("\n");
                        dataOutputStream2.writeBytes(stringBuilder2.toString());
                        dataOutputStream2.flush();
                    }
                    if (readLine.contains("   /system/lib/libcameraservice.so") && readLine.contains("rw-p")) {
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(readLine);
                        stringBuilder2.append("\n");
                        dataOutputStream2.writeBytes(stringBuilder2.toString());
                        dataOutputStream2.flush();
                    }
                    if (readLine.contains("   /system/lib/libcommon_time_client.so") && readLine.contains("rw-p")) {
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(readLine);
                        stringBuilder2.append("\n");
                        dataOutputStream2.writeBytes(stringBuilder2.toString());
                        dataOutputStream2.flush();
                    }
                    if (readLine.contains("   /system/lib/libcompiler_rt.so") && readLine.contains("rw-p")) {
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(readLine);
                        stringBuilder2.append("\n");
                        dataOutputStream2.writeBytes(stringBuilder2.toString());
                        dataOutputStream2.flush();
                    }
                    if (readLine.contains("   /system/lib/libcrypto.so") && readLine.contains("rw-p")) {
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(readLine);
                        stringBuilder2.append("\n");
                        dataOutputStream2.writeBytes(stringBuilder2.toString());
                        dataOutputStream2.flush();
                    }
                } else {
//                    ToastHandler.getInstance().show(ControlPanelActivity.this, /*prompt + */"成功，等待启动游戏！!", Toast.LENGTH_SHORT);
                    return true;
                }
            }

        } catch (Exception e) {

            return false;
        } finally {
            try {
                if (dataInputStream != null) {
                    dataInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (dataOutputStream2 != null) {
                    dataOutputStream2.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Integer getPid(String file) throws IOException {
        DataInputStream dataInputStream = null;
        try {
            dataInputStream = new DataInputStream(new FileInputStream(new File(file)));
            String readLine2 = dataInputStream.readLine();
            if (readLine2 != null) {
                return Integer.valueOf(readLine2);
            }
        } finally {
            try {
                if (dataInputStream != null) {
                    dataInputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        throw new RuntimeException("fail");
    }

    private Boolean startPubg(Integer time) {
        PackageManager packageManager = getPackageManager();
        Intent launchIntentForPackage = packageManager.getLaunchIntentForPackage("com.tencent.tmgp.pubgmhd");
        if (launchIntentForPackage == null) {
            ToastHandler.getInstance().show(this, "请先安装游戏！", Toast.LENGTH_SHORT);
            return false;
        } else {
            startActivity(launchIntentForPackage);
        }
        try {
            TimeUnit.SECONDS.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }


    private String convert(String string) {
        if (string == null) {
            return null;
        } else {
            char[] chars = string.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                chars[i] = (char) (chars[i] ^ 999);
            }
            return new String(chars);
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
            if (string[i].charAt(0) == 'r' && string[i].charAt(1) == 'm') {
                if (new File(string[i].split(" ")[2]).exists()) {
                    return false;
                }
            } else if (string[i].charAt(0) == 'm' && string[i].charAt(1) == 'v') {
                if (new File(string[i].split(" ")[1]).exists()) {
                    return false;
                }
            }
        }
        return true;

    }


    private Boolean checkOperationFrequent() {
        if (System.currentTimeMillis() - lastTime < DEFAULT_FREQUENT_MILLIS) {
//            ToastHandler.getInstance().show(this, "操作太快，请几秒后再试", Toast.LENGTH_SHORT);
            return true;
        } else {
            lastTime = System.currentTimeMillis();
            return false;
        }
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
