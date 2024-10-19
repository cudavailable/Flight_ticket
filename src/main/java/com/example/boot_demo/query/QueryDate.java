package com.example.boot_demo.query;

import com.example.boot_demo.utils.StringToTime;

import java.time.LocalDateTime;

public class QueryDate {
    private String fromTime;

    public QueryDate(){}

    public QueryDate(String fromTime) {
        this.fromTime = fromTime;
    }

    public LocalDateTime stringToDate(){
        StringToTime tran = new StringToTime(fromTime);
        return tran.stringToDate();
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }
}
