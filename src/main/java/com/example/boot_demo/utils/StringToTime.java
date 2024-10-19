package com.example.boot_demo.utils;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class StringToTime {

    private String str;

    public StringToTime(String str) {
        this.str = str;
    }

    public LocalDateTime stringToDate(){
        LocalDateTime dateTime;

        // 判断字符串格式并进行相应处理
        if (str.length() == 10) {
            // 格式为 "yyyy-MM-dd"
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(str, dateFormatter);
            dateTime = LocalDateTime.of(date, LocalTime.MIN);
        } else {
            // 格式为 "yyyy-MM-dd HH:mm:ss"
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            dateTime = LocalDateTime.parse(str, dateTimeFormatter);
        }

        return dateTime;
    }

    public Timestamp stringToTimeStamp(){
        return Timestamp.valueOf(this.stringToDate());
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

}
