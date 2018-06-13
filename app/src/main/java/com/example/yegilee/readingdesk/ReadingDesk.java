package com.example.yegilee.readingdesk;

import android.util.Log;

import java.io.Serializable;

class ReadingDesk implements Serializable {

    private int id;
    private String timer_data;
    private String date;
    private String time;

    public ReadingDesk(int id, String timer_data, String date, String time){
        super();
        this.id=id;
        this.timer_data=timer_data;
        this.date=date;
        this.time=time;
    }

    public ReadingDesk(){super();}


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTimer_data() {
        return timer_data;
    }

    public void setTimer_data(String timer_data) {
        this.timer_data = timer_data;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
