package com.example.boot_demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/*
* f_id INT AUTO_INCREMENT PRIMARY KEY,
	f_name VARCHAR(32) NOT NULL DEFAULT '',
	from_loc VARCHAR(32) NOT NULL DEFAULT '',
	to_loc VARCHAR(32) NOT NULL DEFAULT '',
	from_time DATETIME DEFAULT,
	to_time DATETIME,
	capacity INT DEFAULT 0,
	state INT DEFAULT 0
* */

/*
* Mybatis中数据库的字段名字如果带下划线，
* entity实体类要写成小驼峰命名。
* 比如数据库里写user_name，那么实体类里就要写userName
* */

@TableName("flight")
public class Flight {
    @TableId(value = "f_id", type = IdType.AUTO)
    private long fId;

    @TableField(value = "f_name")
    private String fName;

    @TableField(value = "from_loc")
    private String fromLoc;

    @TableField(value = "to_loc")
    private String toLoc;

    @TableField(value = "from_time")
    private Timestamp fromTime;

    @TableField(value = "to_time")
    private Timestamp toTime;

    private int capacity;

    private int state;

    // addition
    @TableField(value = "base_price")
    private float basePrice;

    public Flight(){}

    public Flight(String fName, String fromLoc, String toLoc, Timestamp fromTime, Timestamp toTime, int capacity, int state, float basePrice) {
        this.fName = fName;
        this.fromLoc = fromLoc;
        this.toLoc = toLoc;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.capacity = capacity;
        this.state = state;
        this.basePrice = basePrice;
    }

    public Flight(long fId, String fName, String fromLoc, String toLoc, Timestamp fromTime, Timestamp toTime, int capacity, int state, float basePrice) {
        this.fId = fId;
        this.fName = fName;
        this.fromLoc = fromLoc;
        this.toLoc = toLoc;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.capacity = capacity;
        this.state = state;
        this.basePrice = basePrice;
    }

    public boolean isFromLocToLocEqual() {
        return this.getFromLoc().equals(this.getToLoc());
    }

    @Override
    public String toString() {
        return "Flight{" +
                "fId=" + fId +
                ", fName='" + fName + '\'' +
                ", fromLoc='" + fromLoc + '\'' +
                ", toLoc='" + toLoc + '\'' +
                ", fromTime=" + fromTime +
                ", toTime=" + toTime +
                ", capacity=" + capacity +
                ", state=" + state +
                ", basePrice=" + basePrice +
                '}';
    }

    public long getfId() {
        return fId;
    }

    public void setfId(long fId) {
        this.fId = fId;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
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

    public Timestamp getFromTime() {
        return fromTime;
    }

    public void setFromTime(Timestamp fromTime) {
        this.fromTime = fromTime;
    }

    public Timestamp getToTime() {
        return toTime;
    }

    public void setToTime(Timestamp toTime) {
        this.toTime = toTime;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public float getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(float basePrice) {
        this.basePrice = basePrice;
    }
}
