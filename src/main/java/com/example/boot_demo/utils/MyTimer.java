package com.example.boot_demo.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MyTimer {

    private String locTime;

    public MyTimer() {
        // 获取当前时刻
        LocalDateTime now = LocalDateTime.now();

        // 定义日期时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 格式化当前时刻为指定格式的字符串
        String formattedDateTime = now.format(formatter);

        this.locTime = formattedDateTime;
    }

    public String getLocTime() {
        return locTime;
    }

    public void setLocTime(String locTime) {
        this.locTime = locTime;
    }
}
