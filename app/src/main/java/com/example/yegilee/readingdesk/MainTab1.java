package com.example.yegilee.readingdesk;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Boolean.TRUE;

public class MainTab1 extends Fragment{

    View rootView;

    //start와 pause를 도와줄 flag
    public int start_pause_flag=0;

    public static MainTab1 mContext;

    //서비스 버튼 - 변수선언
    private Button button_service1;
    private Button button_service2;

    //조명 설정 - 변수 선언
    private Button button_onoff;
    private Button button_reading;
    private Button button_literature;
    private Button button_math;

    //사용시간 설정 - 변수 선언
    public TextView timeCount ;
    Button button_start, button_pause, button_reset, button_lap ;
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;
    Handler handler;
    int Seconds, Minutes, MilliSeconds, Hour;
    ListView listview_timelap ;
    String[] ListElements = new String[] {  };
    List<String> ListElementsArrayList ;
    ArrayAdapter<String> adapter ;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_tab1, container, false);

        mContext=this;

        //서비스 버튼 - 레이아웃과 연결
        button_service1=(Button)rootView.findViewById(R.id.button_service1);
        button_service2=(Button)rootView.findViewById(R.id.button_service2);
        button_service1.setOnClickListener(button_service1Listener);
        button_service2.setOnClickListener(button_service2Listener);


        //조명 설정 - 레이아웃과 연결
        button_onoff=(Button) rootView.findViewById(R.id.button_onoff);
        button_reading=(Button) rootView.findViewById(R.id.button_reading);
        button_literature=(Button) rootView.findViewById(R.id.button_literature);
        button_math=(Button) rootView.findViewById(R.id.button_math);

        button_onoff.setOnClickListener(button_onoffListener);
        button_reading.setOnClickListener(button_readingListener);
        button_literature.setOnClickListener(button_literatureListner);
        button_math.setOnClickListener(button_mathListner);

        //사용시간 설정 - 레이아웃과 연결
        timeCount = (TextView)rootView.findViewById(R.id.timeCount);
        //listview_timelap = (ListView)rootView.findViewById(R.id.listview_timelap);

        //button_start = (Button)rootView.findViewById(R.id.button_start);
        //button_pause = (Button)rootView.findViewById(R.id.button_pause);
        //button_reset = (Button)rootView.findViewById(R.id.button_restart);
        //button_lap = (Button)rootView.findViewById(R.id.button_lap) ;

        //button_start.setOnClickListener(button_startListener);
        //button_pause.setOnClickListener(button_pauseListener);
        //button_reset.setOnClickListener(button_resetListner);
        //button_lap.setOnClickListener(button_lapListener);

        handler = new Handler() ;
        ListElementsArrayList = new ArrayList<String>(Arrays.asList(ListElements));

        adapter = new ArrayAdapter<String>(rootView.getContext(),
                android.R.layout.simple_list_item_1,
                ListElementsArrayList
        );

        //listview_timelap.setAdapter(adapter);

        return rootView;
    }

    //-------------------------------------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------------------------------------

    //서비스 버튼 - 버튼 리스너
    View.OnClickListener button_service1Listener = new View.OnClickListener() {
        public void onClick(View v) {
            startActivity(new Intent(rootView.getContext(), Service1.class));
        }
    };

    View.OnClickListener button_service2Listener = new View.OnClickListener() {
        public void onClick(View v) {
            startActivity(new Intent(rootView.getContext(), Service2.class));

        }
    };


    //사용시간 설정 - 버튼 리스너 메소드
    /*
    View.OnClickListener button_startListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            StartTime = SystemClock.uptimeMillis();
            handler.postDelayed(runnable, 0);

            //button_reset.setEnabled(false);
        }
    };

    View.OnClickListener button_pauseListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TimeBuff += MillisecondTime;

            handler.removeCallbacks(runnable);

            //button_reset.setEnabled(true);
        }
    };
    */

    public void start_timer(){

        //flag가 0이면 현재 타이머가 작동하지않은것으로 판단하여 타이머 start 수행
        if(start_pause_flag==0) {
            StartTime = SystemClock.uptimeMillis();
            handler.postDelayed(runnable, 0);

            start_pause_flag=1;
        }
    }

    public void pause_timer() {

        if (start_pause_flag == 1) {
            TimeBuff += MillisecondTime;
            handler.removeCallbacks(runnable);


            start_pause_flag=0;
        }
    }
/*
    View.OnClickListener button_resetListner=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MillisecondTime = 0L ;
            StartTime = 0L ;
            TimeBuff = 0L ;
            UpdateTime = 0L ;
            Seconds = 0 ;
            Minutes = 0 ;
            MilliSeconds = 0 ;

            timeCount.setText("00:00");

            ListElementsArrayList.clear();

            adapter.notifyDataSetChanged();
        }
    };
*/
/*
    View.OnClickListener button_lapListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ListElementsArrayList.add(timeCount.getText().toString());

            adapter.notifyDataSetChanged();
        }
    };
*/

    public Runnable runnable = new Runnable() {

        public void run() {

            MillisecondTime = SystemClock.uptimeMillis() - StartTime;

            UpdateTime = TimeBuff + MillisecondTime;

            Seconds = (int) (UpdateTime / 1000);

            Minutes = Seconds / 60;

            Seconds = Seconds % 60;

            MilliSeconds = (int) (UpdateTime % 1000);

            Hour=Minutes/60;

            Minutes=Minutes%60;

            timeCount.setText("" + Minutes + ":"+String.format("%02d", Seconds));

            handler.postDelayed(this, 0);
        }

    };


    //조명 설정 - 버튼 리스너 메소드
    View.OnClickListener button_onoffListener = new View.OnClickListener() {
        public void onClick(View v) {

        }
    };

    View.OnClickListener button_readingListener = new View.OnClickListener() {
        public void onClick(View v) {

        }
    };

    View.OnClickListener button_literatureListner = new View.OnClickListener() {
        public void onClick(View v) {

        }
    };

    View.OnClickListener button_mathListner = new View.OnClickListener() {
        public void onClick(View v) {

        }
    };

}
