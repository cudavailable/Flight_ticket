package com.example.boot_demo.query;

import com.example.boot_demo.utils.StringToTime;

import java.time.LocalDateTime;

public class QueryDateFromLocToLoc {
    private String fromTime;
    private String fromLoc;
    private String toLoc;

    public QueryDateFromLocToLoc(){

    }

    public QueryDateFromLocToLoc(String fromTime, String fromLoc, String toLoc) {
        this.fromTime = fromTime;
        this.fromLoc = fromLoc;
        this.toLoc = toLoc;
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

    public String getFromLoc() {
        return fromLoc;
    }

    public void setFromLoc(String fromLoc) {
        this.fromLoc = fromLoc;
    }

    public String getToLoc() {
        return toLoc;
    }

    public void setToLoc(String toLoc) {
        this.toLoc = toLoc;
    }
}
