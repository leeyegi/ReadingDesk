package com.example.yegilee.readingdesk;

import android.content.Entity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Boolean.TRUE;

//메인 페이지에 tab1엑티비티
public class MainTab1 extends Fragment{

    View rootView;

    //start와 pause를 도와줄 flag
    public int start_pause_flag=0;

    //다른 클래스의 메소드를 호출시키기 위한 인스턴스
    public static MainTab1 mContext;
    private SM_Database db;
    private SM_Database2 db2;

    //서비스 버튼 - 변수선언
    private Button button_service1;
    private Button button_service2;

    //현재의 공부 패턴을 확인하기 위한 linechart
    LineChart lineChart;
    ArrayList<Entry> lineEntry;
    ArrayList<String> lineEntityLables;
    LineDataSet lineDataSet;
    LineData lineData;

    /*
    //조명 설정 - 변수 선언
    private Button button_onoff;
    private Button button_reading;
    private Button button_literature;
    private Button button_math;
    */

    //사용시간 설정 - 변수 선언
    public TextView timeCount ;
    //Button button_start, button_pause, button_reset, button_lap ;
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;
    Handler handler;
    int Seconds, Minutes, MilliSeconds, Hour;
    //ListView listview_timelap ;
    String[] ListElements = new String[] {  };
    List<String> ListElementsArrayList ;
    ArrayAdapter<String> adapter ;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_tab1, container, false);

        //다른 클래스에서 현재 클래스를 불러오기 위한 mContext선언
        mContext=this;

        //데이터베이스 삽입 및 쿼리사용을 위한 코드
        db=new SM_Database(getActivity());
        db2=new SM_Database2(getActivity());


        //서비스 버튼 - 레이아웃과 연결
        button_service1=(Button)rootView.findViewById(R.id.button_service1);
        button_service2=(Button)rootView.findViewById(R.id.button_service2);
        button_service1.setOnClickListener(button_service1Listener);
        button_service2.setOnClickListener(button_service2Listener);

        lineChart=(LineChart)rootView.findViewById(R.id.chart_study_pattern);
        drawLinechart();            //메소드 호출

        /*
        //조명 설정 - 레이아웃과 연결
        button_onoff=(Button) rootView.findViewById(R.id.button_onoff);
        button_reading=(Button) rootView.findViewById(R.id.button_reading);
        button_literature=(Button) rootView.findViewById(R.id.button_literature);
        button_math=(Button) rootView.findViewById(R.id.button_math);

        button_onoff.setOnClickListener(button_onoffListener);
        button_reading.setOnClickListener(button_readingListener);
        button_literature.setOnClickListener(button_literatureListner);
        button_math.setOnClickListener(button_mathListner);
        */

        //사용시간 설정 - 레이아웃과 연결
        timeCount = (TextView)rootView.findViewById(R.id.timeCount);
        //listview_timelap = (ListView)rootView.findViewById(R.id.listview_timelap);

        //button_start = (Button)rootView.findViewById(R.id.button_start);
        //button_pause = (Button)rootView.findViewById(R.id.button_pause);
        //button_reset = (Button)rootView.findViewById(R.id.button_restart);
        //button_lap = (Button)rootView.findViewById(R.id.button_lap);

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

    //공부의 집중도를 표시하기 위한 linechart
    public void drawLinechart() {
    lineEntry=new ArrayList<>();
    lineEntityLables = new ArrayList<String>();
    AddValuesToLineDataLabel();

    XAxis xAxis = lineChart.getXAxis();
    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
    xAxis.setDrawAxisLine(true);
    xAxis.setDrawGridLines(false);

    YAxis yAxis=lineChart.getAxisLeft();
    yAxis.setAxisMinValue(0);
    yAxis.setAxisMaxValue(10);


    lineDataSet=new LineDataSet(lineEntry, "나의 공부 패턴");
    lineData=new LineData(lineEntityLables, lineDataSet);
    lineDataSet.setColors(new int[] {Color.parseColor("#5be3c8"), Color.parseColor("#2ad1b0")});
    lineChart.setData(lineData);
    }

    //공부의 집중도를 차트에 표시하기 위한 데이터와 레이블 구성 메소드
    private void AddValuesToLineDataLabel() {
        final ArrayList<ReadingDesk> arReadingDesk=db2.query();
        String data;
        int time=0;

        for(int idx=0;idx<10;idx++) {
            data = arReadingDesk.get(idx).getHhmmss();
            //Log.e("timerdata", data);
            time+=10;

            lineEntry.add(new Entry(Integer.parseInt(data), 9 - idx));
            lineEntityLables.add(String.valueOf(time));
        }

    }

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

    //-------------------------------------------------------------------------------------------------------------

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

    public void save_timer() {

        String save_time=String.valueOf(timeCount.getText());
        db.INSERT(save_time);
        Log.e("db", "insert ok");

        pause_timer();
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

//사용시간을 계산하기 위한 메소드
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

    /*
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
    */
}
