package com.example.boot_demo.query;

import com.example.boot_demo.entity.Flight;
import com.example.boot_demo.utils.Result;
import com.example.boot_demo.utils.StringToTime;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UpdateFlight {
    private long fId;

    private String fName;

    private String fromLoc;

    private String toLoc;

    private String fromTime;

    private String toTime;

    private int capacity;

    private int state;

    private float basePrice;

    public Timestamp strToFromTime(){
        StringToTime tran = new StringToTime(this.fromTime);
        return tran.stringToTimeStamp();
    }

    public Timestamp strToToTime(){
        StringToTime tran = new StringToTime(this.toTime);
        return tran.stringToTimeStamp();
    }

    public Timestamp DateTimeConversion(String inputDateTime){
        // 定义输入日期时间格式
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy, h:mm:ss a");
        // 解析输入日期时间字符串
        LocalDateTime dateTime = LocalDateTime.parse(inputDateTime, inputFormatter);

        // 定义输出日期时间格式
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 格式化日期时间为所需格式
        String outputDateTime = dateTime.format(outputFormatter);

        StringToTime tran = new StringToTime(outputDateTime);
        return tran.stringToTimeStamp();
    }

    public Flight getFlight(){
        return new Flight(
                this.fId,
                this.fName,
                this.fromLoc,
                this.toLoc,
//                this.DateTimeConversion(this.fromTime),
//                this.DateTimeConversion(this.toTime),
                this.strToFromTime(),
                this.strToToTime(),
                this.capacity,
                this.state,
                this.basePrice
        );
    }

    public boolean isFromLocToLocEqual() {
        return this.getFromLoc().equals(this.getToLoc());
    }

    public Result<String> dataOk(){
        if (this.fId <= 0){
            return Result.error("航班Id应该大于0！");
        }
        if (this.fName.isEmpty()){
            return Result.error("请输入有效航班名！");
        }
        if (this.fromLoc.isEmpty()){
            return Result.error("请输入有效起飞地！");
        }
        if (this.toLoc.isEmpty()){
            return Result.error("请输入有效终到地！");
        }
        if (this.fromTime.isEmpty()){
            return Result.error("请输入有效起飞时间！");
        }
        if (this.toTime.isEmpty()){
            return Result.error("请输入有效到达时间！");
        }
        if(this.isFromLocToLocEqual()){
            return Result.error("起点和终点不能相同！");
        }

        if(!this.strToFromTime().before(this.strToToTime())){
            return Result.error("起飞时间应该早于到达时间！");
        }
        if(this.getCapacity() <= 0){
            return Result.error("载客容量应该大于0！");
        }
        if(this.getState() < 0 || this.getState() > 1){
            return Result.error("航班状态设置有误！");
        }
        if(this.getBasePrice() < 0){
            return Result.error("航班基础票价应该大于0!");
        }

        // 检查无误
        return Result.success();
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

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
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
