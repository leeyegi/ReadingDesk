package com.example.yegilee.readingdesk;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
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
    LineChart linechart;
    BarChart barchart;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_tab2, container, false);

        //차트 그리기 - 레이아웃과 연결
        linechart = (LineChart) rootView.findViewById(R.id.linechart);
        drawLinechart();            //메소드 호출

        barchart = (BarChart) rootView.findViewById(R.id.barchart);
        //HorizontalBarChart barChart = (HorizontalBarChart) findViewById(R.id.barchart);
        drawBarchart();

        return rootView;
    }

    //차트 그리기 - 메소드
    private void drawBarchart() {
        String[] labels = new String[]{"2016","2015","2014","2013","2012"};

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, 4f));
        entries.add(new BarEntry(1, 8f));
        entries.add(new BarEntry(2, 6f));
        entries.add(new BarEntry(3, 2f));
        entries.add(new BarEntry(4, 18f));
        entries.add(new BarEntry(5, 9f));
        entries.add(new BarEntry(6, 16f));
        entries.add(new BarEntry(7, 5f));
        entries.add(new BarEntry(8, 3f));
        entries.add(new BarEntry(9, 7f));
        entries.add(new BarEntry(10, 9f));

        BarDataSet bardataset = new BarDataSet(entries, "Cells");

        BarData data = new BarData(bardataset);

        barchart.setData(data); // set the data and list of labels into chart
    }

    public void drawLinechart(){

        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, 4f));
        entries.add(new BarEntry(1, 8f));
        entries.add(new BarEntry(2, 6f));
        entries.add(new BarEntry(3, 2f));
        entries.add(new BarEntry(4, 18f));
        entries.add(new BarEntry(5, 9f));
        entries.add(new BarEntry(6, 16f));
        entries.add(new BarEntry(7, 5f));
        entries.add(new BarEntry(8, 3f));
        entries.add(new BarEntry(9, 7f));
        entries.add(new BarEntry(10, 9f));

        LineDataSet dataset = new LineDataSet(entries, "# of Calls");

        LineData data = new LineData(dataset);
        dataset.setColors(ColorTemplate.COLORFUL_COLORS); //
        /*dataset.setDrawCubic(true); //선 둥글게 만들기
        dataset.setDrawFilled(true); //그래프 밑부분 색칠*/

        linechart.setData(data);
        linechart.animateY(5000);
    }
}
