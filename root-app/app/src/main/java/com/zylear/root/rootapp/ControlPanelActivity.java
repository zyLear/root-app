package com.zylear.root.rootapp;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.zylear.root.rootapp.bean.PassCheckBean;
import com.zylear.root.rootapp.constants.AppConstant;
import com.zylear.root.rootapp.util.JsonUtil;
import com.zylear.root.rootapp.util.OkHttpUtil;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class ControlPanelActivity extends AppCompatActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private Button apply;
    private Button createDirectory;
    private Button deleteDirectory;
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

                requirePermission();
            }
        });

        createDirectory = findViewById(R.id.createDirectory);

        createDirectory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createDirectory();
            }
        });

        deleteDirectory = findViewById(R.id.deleteDirectory);

        deleteDirectory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteDirectory();
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

    private void deleteDirectory() {
        String string = "/data/data/zy";
        DataOutputStream outputStream = null;
        try {
            Process exec = Runtime.getRuntime().exec("su");

            BufferedReader br = new BufferedReader(new InputStreamReader(exec.getInputStream()));
//            new Run(br).start();

            outputStream = new DataOutputStream(exec.getOutputStream());
            outputStream.writeBytes("mount -o remount rw / \n");
            outputStream.writeBytes("rm -rf " + string + "\n");


            Thread.sleep(500);
            File file = new File(string);
            if (file.exists()) {
                Toast.makeText(this, "delete fail", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "delete success", Toast.LENGTH_SHORT).show();
            }


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


    private void createDirectory() {
        DataOutputStream outputStream = null;
        String string = "/data/data/zy";
        try {
            Process exec = Runtime.getRuntime().exec("su");

            outputStream = new DataOutputStream(exec.getOutputStream());
            outputStream.writeBytes("mount -o remount rw / \n");
            outputStream.writeBytes("mkdir " + string + " \n");


            Thread.sleep(500);
            File file = new File(string);
            if (file.exists()) {
                Toast.makeText(this, "create success", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "create fail", Toast.LENGTH_SHORT).show();
            }
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


    private void requirePermission() {
        DataOutputStream outputStream = null;
        try {
            Process exec = Runtime.getRuntime().exec("su");


            long l = System.currentTimeMillis();
//            Thread.sleep(10000);
//            System.out.println("time: " + (System.currentTimeMillis() - l));
//            DataInputStream inputStream = new DataInputStream(exec.getInputStream());
            outputStream = new DataOutputStream(exec.getOutputStream());
//            Log.d("dev", "requirePermission:  after su");
            outputStream.writeBytes("mkdir /data/local/tmp/zy");
//            Log.d("dev", "requirePermission:  after make directory");

//            ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, 1);

            verifyStoragePermissions(this);

//            Thread.sleep(10000);
//            Log.d("dev", "requirePermission:  after requestPermissions");
        } catch (Exception e) {
            Log.e("dev", "requirePermission:  error", e);
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
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
