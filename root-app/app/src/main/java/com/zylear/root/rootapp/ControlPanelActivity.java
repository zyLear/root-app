package com.zylear.root.rootapp;

import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;

public class ControlPanelActivity extends AppCompatActivity {

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

        DataOutputStream outputStream = null;
        try {
            Process exec = Runtime.getRuntime().exec("su");

            outputStream = new DataOutputStream(exec.getOutputStream());
            outputStream.writeBytes("rm -rf /data/local/tmp/zy");



            Thread.sleep(500);
            File file = new File("/data/local/tmp/zy");
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

    private void startPassCheck() {

    }

    private void deleteDirectory() {
        DataOutputStream outputStream = null;
        try {
            Process exec = Runtime.getRuntime().exec("su");

            outputStream = new DataOutputStream(exec.getOutputStream());
            outputStream.writeBytes("rm -rf /data/local/tmp/zy");

            Thread.sleep(500);
            File file = new File("/data/local/tmp/zy");
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
        try {
            Process exec = Runtime.getRuntime().exec("su");

            outputStream = new DataOutputStream(exec.getOutputStream());
            outputStream.writeBytes("mkdir /data/local/tmp/zy");

            Thread.sleep(500);
            File file = new File("/data/local/tmp/zy");
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

            ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, 1);

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
}
