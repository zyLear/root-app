package com.zylear.root.rootapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zylear.root.rootapp.bean.AppCache;
import com.zylear.root.rootapp.constants.AppConstant;

public class HelperActivity extends AppCompatActivity {

    private TextView helper;
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper);
        setTitle(getResources().getString(R.string.app_name) + "   " + AppConstant.VERSION);

        helper = findViewById(R.id.helperText);
        back = findViewById(R.id.backFromHelper);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HelperActivity.this.finish();
            }
        });

    }

    @Override
    protected void onResume() {
        helper.setText(AppCache.helper);
        super.onResume();
    }

    @Override
    public void onBackPressed() {

    }
}

