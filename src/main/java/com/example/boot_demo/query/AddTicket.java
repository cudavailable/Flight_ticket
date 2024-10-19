package com.example.boot_demo.query;

import com.example.boot_demo.entity.Ticketrecord;
import com.example.boot_demo.utils.StringToTime;

import java.sql.Timestamp;

public class AddTicket {
    private Long uId;
    private Long fId;
    private float price;
    private String time;

    public Timestamp stringToTime(){
        StringToTime tran = new StringToTime(time);
        return tran.stringToTimeStamp();
    }

    public Ticketrecord getTicketrecord(){
        return new Ticketrecord(
                this.uId,
                this.fId,
                this.price,
                this.stringToTime()
        );
    }

    public Long getuId() {
        return uId;
    }

    public void setuId(Long uId) {
        this.uId = uId;
    }

    public Long getfId() {
        return fId;
    }

    public void setfId(Long fId) {
        this.fId = fId;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
