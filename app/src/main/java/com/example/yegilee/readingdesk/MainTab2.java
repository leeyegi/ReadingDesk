package com.example.yegilee.readingdesk;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

//메인 엑티비티에 tab2
public class MainTab2 extends Fragment{

    View rootView;

    //주간 공부시간을 확인하기 위한 barchart
    BarChart barchart;
    ArrayList<BarEntry> barEntries;
    ArrayList<String> barEntityLabels;
    BarDataSet barDataSet;
    BarData barData;

    TextView total_time;
    TextView today_date;
    private SM_Database db;
    private ReadingDesk mReadingDesk;



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_tab2, container, false);

        db=new SM_Database(getActivity());


        //차트 그리기 - 레이아웃과 연결
        //linechart = (LineChart) rootView.findViewById(R.id.linechart);
       // drawLinechart();            //메소드 호출

        barchart = (BarChart) rootView.findViewById(R.id.barchart);
        //HorizontalBarChart barChart = (HorizontalBarChart) findViewById(R.id.barchart);
        drawBarchart();

        //오늘 날짜를 표시

        long time_tmp = System.currentTimeMillis();
        SimpleDateFormat day = new SimpleDateFormat("yy-MM-dd");
        String date_val = day.format(new Date(time_tmp));

        today_date=(TextView)rootView.findViewById(R.id.today_date);
        today_date.setText(date_val);


        total_time=(TextView)rootView.findViewById(R.id.total_time_today);
        total_time.setText("00:00:00");
        totalTodayStudy();
        return rootView;
    }

    public void totalTodayStudy(){
        int arg[]={0,0,0};
        long time_tmp = System.currentTimeMillis();
        SimpleDateFormat day = new SimpleDateFormat("yy-MM-dd");
        String date_val = day.format(new Date(time_tmp));
        final ArrayList<ReadingDesk> arReadingDesk=db.query(date_val);

        for (int i=0;i<arReadingDesk.size();i++){
            Log.e("size", String.valueOf(i));
            String tmp=arReadingDesk.get(i).getHhmmss();
            String split[]=tmp.split(":");

            if(split.length==2){
                arg[1]+=Integer.parseInt(split[0]);
                arg[2]+=Integer.parseInt(split[1]);
            }else if(split.length==3){
                arg[0]+=Integer.parseInt(split[0]);
                arg[1]+=Integer.parseInt(split[1]);
                arg[2]+=Integer.parseInt(split[2]);
            }
        }

        if(arg[2]/60!=0){
            arg[1]+=arg[2]/60;
            arg[2]=arg[2]%60;
        }
        if(arg[1]/60!=0){
            arg[0]+=arg[1]/60;
            arg[1]=arg[1]%60;
        }
        Log.e("arg[0]",String.valueOf(arg[0]));
        Log.e("arg[1]",String.valueOf(arg[1]));
        Log.e("arg[2]",String.valueOf(arg[2]));

        total_time.setText(String.valueOf(arg[0])+":"+String.valueOf(arg[1])+":"+String.valueOf(arg[2]));
    }

    //차트 그리기 - 메소드
    private void drawBarchart() {

        barEntries = new ArrayList<>();
        barEntityLabels=new ArrayList<String>();
        AddValuesToBarDataLabel();

        XAxis xAxis=barchart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        barchart.setFitsSystemWindows(true);
        barchart.setDoubleTapToZoomEnabled(false);

        barDataSet= new BarDataSet(barEntries, "공부 시간");
        barData = new BarData(barEntityLabels, barDataSet);
        barDataSet.setColors(new int[] {Color.parseColor("#5be3c8"), Color.parseColor("#2ad1b0")});
        barchart.setData(barData);
    }


    String timer_data;

    public String findStudyTime(String timer_data){
        int timer_int=0;

        String split[]=timer_data.split(":");
        for(int i=0;i<split.length;i++){
            //Log.e("split",split[i]);
        }
        //Log.e("length",String.valueOf(split.length));

        if(split.length==2){
            timer_int=Integer.parseInt(split[0]);
        }else if(split.length==3){
            timer_int=Integer.parseInt(split[0])*60;
            timer_int+=Integer.parseInt(split[1]);
        }

        Log.e("time",String.valueOf(timer_int));

        return String.valueOf(timer_int);
    }

    public void AddValuesToBarDataLabel(){
        long time_tmp = System.currentTimeMillis();
        SimpleDateFormat day = new SimpleDateFormat("yy-MM-dd");
        String date_val = day.format(new Date(time_tmp));

        final ArrayList<ReadingDesk> arReadingDesk=db.query(date_val);


        for(int idx=0;idx<5;idx++){
            timer_data=arReadingDesk.get(idx).getHhmmss();
            Log.e("timerdata",timer_data);

            String timer_str=findStudyTime(timer_data);

            barEntries.add(new BarEntry(Float.parseFloat(timer_str), 4-idx));
            barEntityLabels.add(arReadingDesk.get(4-idx).getTime());

        }

        /*
        barEntries.add(new BarEntry(2f, 0));
        barEntries.add(new BarEntry(4f, 1));
        barEntries.add(new BarEntry(6f, 2));
        barEntries.add(new BarEntry(8f, 3));
        barEntries.add(new BarEntry(7f, 4));
        barEntries.add(new BarEntry(3f, 5));

        barEntityLabels.add("17시");
        barEntityLabels.add("18시");
        barEntityLabels.add("19시");
        barEntityLabels.add("20시");
        barEntityLabels.add("21시");
        barEntityLabels.add("22시");
        */

    }


    //----------------------------------------------------------------------


}
