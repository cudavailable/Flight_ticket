package com.example.boot_demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.jeffreyning.mybatisplus.anno.MppMultiId;

import java.sql.Timestamp;

/*
*   u_id INT,
	f_id INT,
	price FLOAT,
	`time` DATETIME,
* */

@TableName("ticketrecord")
public class Ticketrecord {

    @MppMultiId // 复合主键
    @TableField("u_Id")
    private long uId;

    @MppMultiId // 复合主键
    @TableField("f_id")
    private long fId;

    private float price;

    private Timestamp time;

    @TableField(exist = false)
    private User user;

    @TableField(exist = false)
    private Flight flight;

    public Ticketrecord(long uId, long fId, float price, Timestamp time, User user, Flight flight) {
        this.uId = uId;
        this.fId = fId;
        this.price = price;
        this.time = time;
        this.user = user;
        this.flight = flight;
    }

    public Ticketrecord(long uId, long fId, float price, Timestamp time) {
        this.uId = uId;
        this.fId = fId;
        this.price = price;
        this.time = time;
    }

    @Override
    public String toString() {
        return "Ticketrecord{" +
                "uId=" + uId +
                ", fId=" + fId +
                ", price=" + price +
                ", time=" + time +
                ", user=" + user +
                ", flight=" + flight +
                '}';
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public long getuId() {
        return uId;
    }

    public void setuId(long uId) {
        this.uId = uId;
    }

    public long getfId() {
        return fId;
    }

    public void setfId(long fId) {
        this.fId = fId;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }
}
