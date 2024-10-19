package com.example.boot_demo.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.boot_demo.entity.Flight;
import com.example.boot_demo.mapper.FlightMapper;
import com.example.boot_demo.query.QueryDateFromLocToLoc;
import com.example.boot_demo.utils.ConsoleLogger;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class FlightService {

    private final FlightMapper flightMapper;


    public FlightService(FlightMapper flightMapper) {
        this.flightMapper = flightMapper;
    }

    public List<Flight> selectTransferOnly(QueryDateFromLocToLoc queryDateFromLocToLoc){

        /* part 1 */
        LocalDateTime dateTime = queryDateFromLocToLoc.stringToDate();

        // 获取 fromTime 的日期部分
        LocalDate date = dateTime.toLocalDate();
        LocalDateTime startOfDay = date.atStartOfDay();

        // 条件查询
        QueryWrapper<Flight> qw1 = new QueryWrapper<>();
        qw1.eq("from_loc", queryDateFromLocToLoc.getFromLoc())
                .ge("from_time", startOfDay)
                .lt("from_time", startOfDay.plusDays(1));

        List<Flight> list1 = flightMapper.selectList(qw1);
//        ConsoleLogger.logError("Test ---- list1: " + list1);

        /* part 2 */
        QueryWrapper<Flight> qw2 = new QueryWrapper<>();
        qw2.eq("to_loc", queryDateFromLocToLoc.getToLoc());
        List<Flight> list2 = flightMapper.selectList(qw2);
//        ConsoleLogger.logError("Test ---- list2: " + list2);

        List<Flight> bestCouple = new ArrayList<>();

        for(Flight f1 : list1){

            for(Flight f2 : list2){
                List<Flight> couple = new ArrayList<>();

                // 需要确定唯一一个中转点
                if (!f1.getToLoc().equals(f2.getFromLoc())){
//                    ConsoleLogger.logError("In if 1");
                    continue;
                }

                // 需要第一次的落地时间早于第二次的起飞时间，且时间差在一个范围(1h< dt_time< 12h)内
                int timeDiff = getTimeDifference(f2.getFromTime(), f1.getToTime());
                if ( !( (f1.getToTime().before(f2.getFromTime())) && timeDiff >= 1 && timeDiff < 12 ) ){
                    // 这里出错
//                    ConsoleLogger.logError("In if 2");
                    continue;
                }

                // 符合基本条件，创建一个对象
                couple.add(f1);
                couple.add(f2);

//                ConsoleLogger.logError("---- " + couple);

                if (bestCouple.isEmpty()){
                    // 最佳结果为空，则直接添加
                    bestCouple.addAll(couple);
                }else {
                    // 最佳结果已有对象填入，则需比较总花费
                    if(flightTotalCost(couple) < flightTotalCost(bestCouple)){
                        bestCouple.clear();
                        bestCouple.addAll(couple);
                    }

                }

            }

        }

        // 最佳结果可能为空，也可能有两趟班机
        return bestCouple;
    }

    public List<Flight> selectDirectFlight(QueryDateFromLocToLoc queryDateFromLocToLoc){
        LocalDateTime dateTime = queryDateFromLocToLoc.stringToDate();

        // 获取 fromTime 的日期部分
        LocalDate date = dateTime.toLocalDate();
        LocalDateTime startOfDay = date.atStartOfDay();

        // 条件查询
        QueryWrapper<Flight> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("from_loc", queryDateFromLocToLoc.getFromLoc())
                .eq("to_loc", queryDateFromLocToLoc.getToLoc())
                .ge("from_time", startOfDay)
                .lt("from_time", startOfDay.plusDays(1));

        return flightMapper.selectList(queryWrapper);
    }

    public int getTimeDifference(Timestamp endTime, Timestamp startTime) {
        long diff;
        long t1 = endTime.getTime();
        long t2 = startTime.getTime();

        if (t1 < t2) {
            diff = t2 - t1;
        } else {
            diff = t1 - t2;
        }

        return (int) (diff / (1000 * 60 * 60));
    }

    public float flightTotalCost(List<Flight> list){
        if (list.isEmpty()){
            return 0;
        }

        float ans = 0;
        for (Flight f : list){
            ans += f.getBasePrice();
        }

        return ans;
    }

}
