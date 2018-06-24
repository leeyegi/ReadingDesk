package com.example.yegilee.readingdesk;

import android.util.Log;

import java.io.Serializable;

class ReadingDesk implements Serializable {

    //table 1
    private int id;
    private String hhmmss;
    private String date;
    private String time;

    //table 2
    private int Wid;
    private String Whhmmss;
    private String Wdate;

    //table 3
    private int Pid;
    private String Pdata;
    private String Pdate;
    private String Ptime;

    public ReadingDesk(int id, String hhmmss, String date, String time) {
        super();
        this.id = id;
        this.hhmmss = hhmmss;
        this.date = date;
        this.time = time;
    }

    public ReadingDesk(int Wid, String Whhmmss, String Wdate) {
        super();
        this.Wid = Wid;
        this.Whhmmss = Whhmmss;
        this.Wdate = Wdate;
    }
/*
    public ReadingDesk(int Pid, String Pdata, String Pdate, String Ptime){
        super();
        this.Pid=Pid;
        this.Pdata=Pdata;
        this.Pdate=Pdate;
        this.Ptime=Ptime;
    }*/

    public ReadingDesk() {
        super();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHhmmss() {
        return hhmmss;
    }

    public void setHhmmss(String hhmmss) {
        this.hhmmss = hhmmss;
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

    public int getWid() {
        return Wid;
    }

    public void setWid(int wid) {
        Wid = wid;
    }

    public String getWhhmmss() {
        return Whhmmss;
    }

    public void setWhhmmss(String whhmmss) {
        Whhmmss = whhmmss;
    }

    public String getWdate() {
        return Wdate;
    }

    public void setWdate(String wdate) {
        Wdate = wdate;
    }

    public int getPid() {
        return Pid;
    }

    public void setPid(int pid) {
        Pid = pid;
    }

    public String getPdata() {
        return Pdata;
    }

    public void setPdata(String pdata) {
        Pdata = pdata;
    }

    public String getPdate() {
        return Pdate;
    }

    public void setPdate(String pdate) {
        Pdate = pdate;
    }

    public String getPtime() {
        return Ptime;
    }

    public void setPtime(String ptime) {
        Ptime = ptime;
    }
}

