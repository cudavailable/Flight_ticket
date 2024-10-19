package com.example.boot_demo.query;

import com.example.boot_demo.utils.StringToTime;
import lombok.Getter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class TicketAny {
    private String fromLoc;
    private String toLoc;
    private String fromTime;
    private String time;

    public TicketAny(){

    }

    public TicketAny(String fromLoc, String toLoc, String fromTime, String time) {
        this.fromLoc = fromLoc;
        this.toLoc = toLoc;
        this.fromTime = fromTime;
        this.time = time;
    }

    public Timestamp stringToDate(){
        StringToTime tran = new StringToTime(fromTime);
        return tran.stringToTimeStamp();
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public void setFromLoc(String fromLoc) {
        this.fromLoc = fromLoc;
    }

    public void setToLoc(String toLoc) {
        this.toLoc = toLoc;
    }

    public String getFromLoc() {
        return fromLoc;
    }

    public String getToLoc() {
        return toLoc;
    }

    public String getFromTime() {
        return fromTime;
    }

    public String getTime() {
        return time;
    }
}

