package com.zylear.root.rootapp;

import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requirePermission();


    }

    private void requirePermission() {
        DataOutputStream outputStream = null;
        try {
//            Process exec = Runtime.getRuntime().exec("su");
//            Process exec = Runtime.getRuntime().exec("ls");

            long l = System.currentTimeMillis();
//            Thread.sleep(10000);
//            System.out.println("time: " + (System.currentTimeMillis() - l));
//            DataInputStream inputStream = new DataInputStream(exec.getInputStream());
//            outputStream = new DataOutputStream(exec.getOutputStream());
//            Log.d("dev", "requirePermission:  after su");
//            outputStream.writeBytes("mkdir /data/local/tmp/zy");
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
