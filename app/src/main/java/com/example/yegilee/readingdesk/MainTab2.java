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

import java.util.ArrayList;

public class MainTab2 extends Fragment{


    View rootView;

    //차트 그리기 - 변수 선언
    //LineChart linechart;

    BarChart barchart;
    ArrayList<BarEntry> barEntries;
    ArrayList<String> barEntityLabels;
    BarDataSet barDataSet;
    BarData barData;

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

        return rootView;
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



    public void AddValuesToBarDataLabel(){
        /*
        final ArrayList<ReadingDesk> arReadingDesk=db.query();


        for(int idx=0;idx<5;idx++){
                barEntries.add(new BarEntry(Float.parseFloat(arReadingDesk.get(idx).getTimer_data()), idx));
                barEntityLabels.add(arReadingDesk.get(idx).getTime());
        }
        */
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
