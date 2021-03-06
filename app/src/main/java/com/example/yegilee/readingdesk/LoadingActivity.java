package com.example.yegilee.readingdesk;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

//로딩화면 - 3초 후에 엑티비티 넘어감
public class LoadingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        //handler을 통해 3초후에 메인 엑티비티로 넘어감
        @SuppressLint("HandlerLeak") Handler mhandler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                startActivity(new Intent(LoadingActivity.this, MainActivity.class));
                finish();   // activity 종료
            }
        };
        mhandler.sendEmptyMessageDelayed(0, 1500);    // ms, 3초후 종료시킴
    }
}