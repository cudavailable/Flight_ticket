package com.example.boot_demo.controller;

import com.example.boot_demo.entity.Flight;
import com.example.boot_demo.entity.Ticketrecord;
import com.example.boot_demo.entity.User;
import com.example.boot_demo.mapper.FlightMapper;
import com.example.boot_demo.mapper.TicketrecordMapper;
import com.example.boot_demo.mapper.UserMapper;
import com.example.boot_demo.query.AddTicket;
import com.example.boot_demo.query.QueryDateFromLocToLoc;
import com.example.boot_demo.query.QueryUserLogin;
import com.example.boot_demo.service.FlightService;
import com.example.boot_demo.service.TicketrecordService;
import com.example.boot_demo.utils.JwtUtil;
import com.example.boot_demo.utils.MyTimer;
import com.example.boot_demo.utils.ThreadLocalUtil;
import com.google.gson.Gson;
import org.apache.ibatis.executor.BatchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.codec.digest.DigestUtils;

import com.example.boot_demo.utils.Result;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@CrossOrigin(origins = {"*","null"})//跨域
@RequestMapping("/user")
public class UserController {

    private final TicketrecordService ticketrecordService;

    private final FlightService flightService;

//    @Autowired
//    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FlightMapper flightMapper;

    @Autowired
    private TicketrecordMapper ticketrecordMapper;

    @Autowired
    private Gson gson;

    public UserController(TicketrecordService ticketrecordService, FlightService flightService) {
        this.ticketrecordService = ticketrecordService;
        this.flightService = flightService;
    }

    @PostMapping("/register")
    public Result<String> register(@RequestBody User user){

        // 注册账号，密码，姓名，电话不能为空
        if (user.getAccount().isEmpty()){
            return Result.error("注册失败，账号不能为空！");
        }
        if (user.getPwd().isEmpty()){
            return Result.error("注册失败，密码不能为空！");
        }
        if (user.getName().isEmpty()){
            return Result.error("注册失败，姓名不能为空！");
        }
        if (user.getTele().isEmpty()){
            return Result.error("注册失败，电话号码不能为空！");
        }

        // 排查账号是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", user.getAccount());
        long count = userMapper.selectCount(queryWrapper);
        if(count > 0){
            return Result.error("注册失败，该账号已存在！");
        }

        // 加密用户输入的密码后再存入数据库
        String plainPassword = user.getPwd();
        String hashedPassword = DigestUtils.md5Hex(plainPassword);
        user.setPwd(hashedPassword);

        int i = userMapper.insert(user);
        if(i > 0){
            return Result.success();
        }else {
            return Result.error("账号创建失败！");
        }
    }

    @PostMapping("/login")
    public Result<String> login(@RequestBody QueryUserLogin queryUserLogin){
        // 加密用户输入的密码后与数据库的数据比对
        String plainPassword = queryUserLogin.getPwd();
        String hashedPassword = DigestUtils.md5Hex(plainPassword);
        queryUserLogin.setPwd(hashedPassword);

        // 条件查询
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", queryUserLogin.getAccount()).eq("pwd", queryUserLogin.getPwd());
        long count = userMapper.selectCount(queryWrapper);

        // 判断是否登录成功
        if (count > 0) {
            // 找到用户对应的id
            User user = userMapper.selectOne(queryWrapper);

            // 生成jwt令牌,并返给前端
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", user.getId());
            claims.put("name", user.getName());
            String token = JwtUtil.getToken(claims);

//            //把token存储到redis中
//            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
//            operations.set(token, token, 12, TimeUnit.HOURS);

            return Result.success(token);

        } else {
            return Result.error("请输入正确的账号和密码！");
        }

    }

    @GetMapping("/query_all")
    public Result<String> queryAll(){
        List<Flight> list = flightMapper.selectList(null);
        return Result.success(gson.toJson(list));
    }

    @PostMapping("/query_date_fromLoc_toLoc")
    public Result<String> queryDateFromLocToLoc(@RequestBody QueryDateFromLocToLoc queryDateFromLocToLoc){

        QueryWrapper<Flight> queryWrapper = new QueryWrapper<>();

        // 查看传入条件是否为空
        if (!queryDateFromLocToLoc.getFromTime().isEmpty()){
            LocalDateTime dateTime = queryDateFromLocToLoc.stringToDate();

            // 获取 fromTime 的日期部分
            LocalDate date = dateTime.toLocalDate();
            LocalDateTime startOfDay = date.atStartOfDay();

            queryWrapper
                    .ge("from_time", startOfDay)
                    .lt("from_time", startOfDay.plusDays(1));
        }
        if (!queryDateFromLocToLoc.getFromLoc().isEmpty()){
            queryWrapper.eq("from_loc", queryDateFromLocToLoc.getFromLoc());
        }
        if (!queryDateFromLocToLoc.getToLoc().isEmpty()){
            queryWrapper.eq("to_loc", queryDateFromLocToLoc.getToLoc());
        }


//        LocalDateTime dateTime = queryDateFromLocToLoc.stringToDate();
//
//        // 获取 fromTime 的日期部分
//        LocalDate date = dateTime.toLocalDate();
//        LocalDateTime startOfDay = date.atStartOfDay();
//
//        // 条件查询
//        QueryWrapper<Flight> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("from_loc", queryDateFromLocToLoc.getFromLoc())
//                .eq("to_loc", queryDateFromLocToLoc.getToLoc())
//                .ge("from_time", startOfDay)
//                .lt("from_time", startOfDay.plusDays(1));

        List<Flight> list = flightMapper.selectList(queryWrapper);
        return Result.success(gson.toJson(list));
    }

    @PostMapping("/get_own_ticket_record")
    public Result<String> getOwnTicketRecord(/*@RequestHeader(name = "Authorization") String token*/){
        // 获取token中的用户id
//        Map<String, Object> map = JwtUtil.parseToken(token);
//        Long uId = ((Integer) map.get("id")).longValue();
        Map<String, Object> map = ThreadLocalUtil.get();
        Long uId = ((Integer) map.get("id")).longValue();

        // 根据用户ID查询订单
        List<Ticketrecord> list = ticketrecordService.selectOwnTicketRecord(uId);
        return Result.success(gson.toJson(list));
    }

    @PutMapping("/update_user")
    public Result<String> updateUser(@RequestBody User user){
        // 获取用户id
        Map<String, Object> map = ThreadLocalUtil.get();
        long uId = ((Integer) map.get("id")).longValue();
        user.setId(uId);

//        // 更改的账号不能与其他用户的账号相同
//        QueryWrapper<User> qw = new QueryWrapper<>();
//        qw.eq("account", user.getAccount());
//        long count = userMapper.selectCount(qw);
//        if (count > 0){
//            return Result.error("该账号已存在！");
//        }

        // 加密用户输入的密码后与数据库的数据比对
        String plainPassword = user.getPwd();
        String hashedPassword = DigestUtils.md5Hex(plainPassword);
        user.setPwd(hashedPassword);

        // 更新用户信息
//        Collection<User> entityList = Collections.singletonList(user);
//        List<BatchResult> batchResults = userMapper.updateById(entityList);
//        BatchResult batchResult = batchResults.isEmpty() ? null : batchResults.get(0);

        int i = userMapper.updateById(user);

        // 处理更新结果
        if (i > 0) {
//            System.out.println("Affected Rows: " + Arrays.toString(batchResult.getUpdateCounts()));
            return Result.success();
        } else {
//            System.out.println("Update failed.");
            return Result.error("更新失败！");
        }
    }

    @PatchMapping("/updatePwd")
    public Result<String> updatePwd(@RequestBody Map<String, String> params){
        //1.校验参数
        String oldPwd = params.get("old_pwd");
        String newPwd = params.get("new_pwd");
        String rePwd = params.get("re_pwd");

//        System.out.println("old_pwd : " + oldPwd);
//        System.out.println("new_pwd : " +newPwd);
//        System.out.println("re_pwd : " + rePwd);

        if (oldPwd.length() <=0 || newPwd.length() <=0 || rePwd.length() <=0){
            return Result.error("缺少必要的参数！");
        }

        // 原密码是否正确
        Map<String, Object> map = ThreadLocalUtil.get();
        long uId = ((Integer) map.get("id")).longValue();
//        System.out.println("u_id : " + uId);

        // 根据token中的用户id从数据库中获取到的相应的用户
        User loginUser = userMapper.selectById(uId);
//        System.out.println(loginUser);
//        System.out.println("\n\n");

        // 对用户写入的oldPwd进行加密
        String hashedPassword = DigestUtils.md5Hex(oldPwd);

        if (!loginUser.getPwd().equals(hashedPassword)){
            return Result.error("原密码填写不正确");
        }

        // newPwd和rePwd是否一样
        if (!rePwd.equals(newPwd)){
            return Result.error("两次填写的新密码不一致");
        }

        //2.完成密码更新
        String hashedPwd = DigestUtils.md5Hex(newPwd);
        loginUser.setPwd(hashedPwd);

        int i = userMapper.updateById(loginUser);

        if (i > 0){
            return Result.success();
        }else {
            return Result.error("密码更新失败！");
        }
    }

    @PostMapping("/buy_ticket")
    public Result<String> insertTicket(@RequestBody AddTicket addTicket){
        // 解析token获取其中的用户id
        Map<String, Object> map = ThreadLocalUtil.get();
        Long uId = ((Integer) map.get("id")).longValue();

        // 设置真正的用户id
        addTicket.setuId(uId);

        // 设置时间为当前时刻
        MyTimer timer = new MyTimer();
        addTicket.setTime(timer.getLocTime());

        // 购票时间需要在航班起飞时间之前
        if (!ticketrecordService.isTimeRemaining(addTicket.getTime(), addTicket.getfId())){
            return Result.error("当前航班已起飞，无法再订票!");
        }

        // 用户不能重复购票
        if (ticketrecordService.existTicketRecord(addTicket.getuId(), addTicket.getfId())){
            return Result.error("已购买当前航班机票，无需重复购票！");
        }

        // 判断当前航班是否还有载客容量
        if (!ticketrecordService.isCapacityRemaining(addTicket.getfId())){
            return Result.error("当前航班已没有载客余量！");
        }

        boolean buy = ticketrecordService.insertOneTicketRecord(addTicket);

        if(buy){
            return Result.success();
        }else {
            return Result.error("购票失败！");
        }
    }

    @DeleteMapping("/refund/{f_id}")
    public Result<String> deleteTicket(@PathVariable("f_id") Long fId){
        // 从token中解析用户id
        Map<String, Object> map = ThreadLocalUtil.get();
        Long uId = ((Integer) map.get("id")).longValue();

        Result<String> res = ticketrecordService.deleteOneTicketRecord(uId, fId);
        return res;

    }

    @PostMapping("/query_transfer")
    public Result<String> query_transfer(@RequestBody QueryDateFromLocToLoc queryDateFromLocToLoc){
        if (queryDateFromLocToLoc.getFromTime().isEmpty()){
            return Result.error("出发时间不能为空！");
        }
        if (queryDateFromLocToLoc.getFromLoc().isEmpty()){
            return Result.error("出发地不能为空！");
        }
        if (queryDateFromLocToLoc.getToLoc().isEmpty()){
            return Result.error("目的地不能为空！");
        }

        // 只获取转机方案，最多有一个方案
        List<Flight> bestCouple = flightService.selectTransferOnly(queryDateFromLocToLoc);

        // 只获取直达方案，可能有很多个方案，也可能没有
        List<Flight> directFlight = flightService.selectDirectFlight(queryDateFromLocToLoc);

        // 比较最优方案
        if (bestCouple.isEmpty() && directFlight.isEmpty()){
            return Result.error("没有直达航班和合理的转机方案！");
        }
        if (directFlight.isEmpty()){
            // 如果没有直达方案,且有转机方案
            return Result.success(gson.toJson(bestCouple));
        }

        // 如果有直达方案
        float tranCost = flightService.flightTotalCost(bestCouple); // 转机花费

        Flight direct = directFlight.get(0); // 直达最佳方案
        for(Flight flight : directFlight){
            if (flight.getBasePrice() < direct.getBasePrice()){
                direct = flight;
            }
        }

        // 且如果没有转机方案
        if (bestCouple.isEmpty()){
            directFlight.clear();
            directFlight.add(direct);
            return Result.success(gson.toJson(directFlight));
        }

        // 如果有转机方案，则比较总花费
        if (tranCost < direct.getBasePrice()){
            return Result.success(gson.toJson(bestCouple));
        }else {
            directFlight.clear();
            directFlight.add(direct);
            return Result.success(gson.toJson(directFlight));
        }

    }

    @GetMapping("/get_my_info")
    public Result<String> getMyInfo(){
        // 获取用户id
        Map<String, Object> map = ThreadLocalUtil.get();
        long uId = ((Integer) map.get("id")).longValue();

        User user = userMapper.selectById(uId);
        if (user != null){
            return Result.success(gson.toJson(user));
        }else {
            return Result.error("无法获取我的信息！");
        }

    }

}
