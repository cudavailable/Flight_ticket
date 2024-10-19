package com.example.boot_demo.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.boot_demo.entity.Flight;
import com.example.boot_demo.entity.Ticketrecord;
import com.example.boot_demo.entity.User;
import com.example.boot_demo.mapper.FlightMapper;
import com.example.boot_demo.mapper.TicketrecordMapper;
import com.example.boot_demo.mapper.UserMapper;
import com.example.boot_demo.query.TicketAny;
import com.example.boot_demo.utils.ConsoleLogger;
import com.example.boot_demo.utils.Result;
import com.example.boot_demo.utils.StringToTime;
import com.example.boot_demo.query.AddTicket;
import com.example.boot_demo.utils.MyTimer;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class TicketrecordService {
    private final TicketrecordMapper ticketrecordMapper;
    private final UserMapper userMapper;
    private final FlightMapper flightMapper;

    public TicketrecordService(TicketrecordMapper ticketrecordMapper, UserMapper userMapper, FlightMapper flightMapper) {
        this.ticketrecordMapper = ticketrecordMapper;
        this.userMapper = userMapper;
        this.flightMapper = flightMapper;
    }

    /*
     * 查询所有用户购票记录
     * u_id, name, tele,
     * f_id, f_name, from_loc, to_loc, from_time, to_time
     **/
    public List<Ticketrecord> selectAllTicketRecord() {

        // 查询tr表中所有记录
        List<Ticketrecord> list = ticketrecordMapper.selectList(null);

        // 用list储存的u_id和f_id去关联(by id)，完成多表查询
        for (Ticketrecord ticketrecord : list) {

            // 用list储存的u_id去取user表中的数据(by id)
            Long uId = ticketrecord.getuId();
            User user = userMapper.selectById(uId);
            ticketrecord.setUser(user);

            // 用list储存的f_id去取flight表中的数据(by id)
            Long fId = ticketrecord.getfId();
            Flight flight = flightMapper.selectById(fId);
            ticketrecord.setFlight(flight);
        }

        return list;
    }

    /*
     * 查询指定用户的购票记录
     * u_id, name, tele,
     * f_id, f_name, from_loc, to_loc, from_time, to_time
     **/
    public List<Ticketrecord> selectOwnTicketRecord(Long uId) {

        // 查询tr表中uId用户的所有记录
        QueryWrapper<Ticketrecord> qw = new QueryWrapper<>();
        qw.eq("u_id", uId);
        List<Ticketrecord> list = ticketrecordMapper.selectList(qw);

        // 用list储存的u_id和f_id去关联(by id)，完成多表查询
        for (Ticketrecord ticketrecord : list) {

            // 用list储存的u_id去取user表中的数据(by id)
            Long uerId = ticketrecord.getuId();
            User user = userMapper.selectById(uerId);
            ticketrecord.setUser(user);

            // 用list储存的f_id去取flight表中的数据(by id)
            Long fId = ticketrecord.getfId();
            Flight flight = flightMapper.selectById(fId);
            ticketrecord.setFlight(flight);
        }

        return list;
    }

    public List<Ticketrecord> selectTicketRecordDate(LocalDateTime dateTime) {

        // 获取 fromTime 的日期部分
        LocalDate date = dateTime.toLocalDate();
        LocalDateTime startOfDay = date.atStartOfDay();

        // 条件查询
        QueryWrapper<Ticketrecord> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .ge("time", startOfDay)
                .lt("time", startOfDay.plusDays(1));

        // 时间作为查询条件
        List<Ticketrecord> list =  ticketrecordMapper.selectList(queryWrapper);

        // 用list储存的u_id和f_id去关联(by id)，完成多表查询
        for (Ticketrecord ticketrecord : list) {

            // 用list储存的u_id去取user表中的数据(by id)
            Long uerId = ticketrecord.getuId();
            User user = userMapper.selectById(uerId);
            ticketrecord.setUser(user);

            // 用list储存的f_id去取flight表中的数据(by id)
            Long fId = ticketrecord.getfId();
            Flight flight = flightMapper.selectById(fId);
            ticketrecord.setFlight(flight);
        }

        return list;
    }

    public List<Ticketrecord> selectTicketRecordAny(TicketAny ticketAny){

        QueryWrapper<Ticketrecord> queryWrapper = new QueryWrapper<>();

        if (!ticketAny.getTime().isEmpty()){
            // 转化对象中的time类型为LocalDate
            StringToTime tran = new StringToTime(ticketAny.getTime());
            LocalDate date = tran.stringToDate().toLocalDate();
            LocalDateTime startOfDay = date.atStartOfDay();

            // 条件查询
            queryWrapper
                    .ge("time", startOfDay)
                    .lt("time", startOfDay.plusDays(1));
        }

        // 购票时间作为查询条件
        List<Ticketrecord> list =  ticketrecordMapper.selectList(queryWrapper);

        List<Ticketrecord> finalList = new ArrayList<>();

        for (Ticketrecord ticketrecord : list){
            // 用list储存的f_id去取flight表中的数据(by id)
            Long fId = ticketrecord.getfId();
            Flight flight = flightMapper.selectById(fId);

            // 看根据fId查到的航班记录是否满足三条件(先判断是否有该条件要求)

            if (!ticketAny.getFromTime().isEmpty()){
                LocalDate date1 = (ticketAny.stringToDate()).toLocalDateTime().toLocalDate();
                LocalDate date2 = (flight.getFromTime()).toLocalDateTime().toLocalDate();

                if (!date1.isEqual(date2)){
                    continue;
                }
            }
            if (!ticketAny.getFromLoc().isEmpty()){
                if (!flight.getFromLoc().equals(ticketAny.getFromLoc())){
                    continue;
                }
            }
            if (!ticketAny.getToLoc().isEmpty()){
                if (!flight.getToLoc().equals(ticketAny.getToLoc())){
                    continue;
                }
            }

            finalList.add(ticketrecord);
        }

        // 用list储存的u_id和f_id去关联(by id)，完成多表查询
        for (Ticketrecord ticketrecord : finalList) {

            // 用list储存的u_id去取user表中的数据(by id)
            Long uerId = ticketrecord.getuId();
            User user = userMapper.selectById(uerId);
            ticketrecord.setUser(user);

            // 用list储存的f_id去取flight表中的数据(by id)
            Long fId = ticketrecord.getfId();
            Flight flight = flightMapper.selectById(fId);
            ticketrecord.setFlight(flight);
        }

        return finalList;
    }

    public boolean insertOneTicketRecord(AddTicket addTicket){

        // 根据前端传入的两个id进行查询，拿到相应的对象
        User uer = userMapper.selectById(addTicket.getuId());
        Flight flight = flightMapper.selectById(addTicket.getfId());

        // 修改航班客余量
        flight.setCapacity(flight.getCapacity() - 1);
        int count = flightMapper.updateById(flight);

        // 更新失败
        if (count <= 0){
            return false;
        }

        // 构造Ticketrecord对象
        Ticketrecord tr = addTicket.getTicketrecord();

        // 插入Ticketrecord对象
        int i = ticketrecordMapper.insert(tr);

        // 插入失败
        if (i <= 0){
            return false;
        }

        return true;
    }

    public Result<String> deleteOneTicketRecord(long uId, long fId){

        if (uId <= 0){
            return Result.error("u_id应该大于0！");
        }
        if (fId <= 0){
            return Result.error("f_id应该大于0！");
        }

        // 如果不存在该购票记录，则不能删除
        if (!existTicketRecord(uId, fId)){
            return Result.error("不用退票，用户没有该购票记录！");
        }

        // 当前航班起飞时间
        Flight flight = flightMapper.selectById(fId);
        Timestamp fromTime = flight.getFromTime();

        // 购票时间
        MyTimer timer = new MyTimer();
        StringToTime tran = new StringToTime(timer.getLocTime());
        Timestamp buyTime = tran.stringToTimeStamp();

        // 如果购票时间在飞机起飞时间之后，不能退票
        if (fromTime.before(buyTime)){
            return Result.error("购票时间在飞机起飞之后，不能退票！");
        }

        // 当前航班容量+1
        flight.setCapacity(flight.getCapacity() + 1);
        int i = flightMapper.updateById(flight);

        // 更新失败
        if (i <= 0){
            return Result.error("退票失败，航班容量出错！");
        }

        // 条件删除
        QueryWrapper<Ticketrecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("u_id", uId).eq("f_id", fId);
        int count = ticketrecordMapper.delete(queryWrapper);

        if (count > 0){
            return Result.success();
        }else {
            return Result.error("退票失败！");
        }
    }

    public boolean existTicketRecord(long uId, long fId){

        // 查询tr表中uId用户的所有记录
        QueryWrapper<Ticketrecord> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("u_id", uId)
                .eq("f_id", fId);

        // 如果已经存在该记录，则返回true
        if (ticketrecordMapper.exists(queryWrapper)){
            return true;
        }

        return false;
    }

    public boolean isTimeRemaining(String buyTime, long fId){
        // 将传入的时间戳字符串转化为Timestamp，购买时间
        StringToTime tran = new StringToTime(buyTime);
        Timestamp buy = tran.stringToTimeStamp();

        // 从数据库中找到对应id的航班，起飞时间
        Flight flight = flightMapper.selectById(fId);
        Timestamp fromTime = flight.getFromTime();

        if (buy.before(fromTime)){
            return true;
        }

        return false;
    }

    public boolean isCapacityRemaining(long fId){
        // 从数据库中找到对应id的航班，起飞时间
        Flight flight = flightMapper.selectById(fId);
        int capacity = flight.getCapacity();

        if (capacity > 0){
            return true;
        }

        return false;
    }

}
