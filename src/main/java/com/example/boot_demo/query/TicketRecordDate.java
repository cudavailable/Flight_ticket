package com.example.boot_demo.query;

import com.example.boot_demo.utils.StringToTime;

import java.time.LocalDateTime;

public class TicketRecordDate {
    private String time;

    public TicketRecordDate(){

    }

    public TicketRecordDate(String time) {
        this.time = time;
    }

    public LocalDateTime stringToDate(){
        StringToTime tran = new StringToTime(time);
        return tran.stringToDate();
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
