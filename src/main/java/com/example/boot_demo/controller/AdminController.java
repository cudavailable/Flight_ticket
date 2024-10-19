package com.example.boot_demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.boot_demo.entity.Admin;
import com.example.boot_demo.entity.Flight;
import com.example.boot_demo.entity.Ticketrecord;
import com.example.boot_demo.mapper.AdminMapper;
import com.example.boot_demo.mapper.FlightMapper;
import com.example.boot_demo.mapper.TicketrecordMapper;
import com.example.boot_demo.utils.Result;
import com.example.boot_demo.query.*;
import com.example.boot_demo.service.TicketrecordService;
import com.example.boot_demo.utils.JwtUtil;
import com.example.boot_demo.utils.ThreadLocalUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.google.gson.Gson;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@CrossOrigin(origins = {"*","null"})//跨域
@RequestMapping("/admin")
public class AdminController {

    private final TicketrecordService ticketrecordService;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private FlightMapper flightMapper;

    @Autowired
    private Gson gson;

    @Autowired
    private TicketrecordMapper ticketrecordMapper;

    public AdminController(TicketrecordService ticketrecordService) {
        this.ticketrecordService = ticketrecordService;
    }

    @PostMapping("/register")
    public Result<String> register(@RequestBody Admin admin){
        // 注册账号，密码不能为空
        if (admin.getAccount().isEmpty()){
            return Result.error("注册失败，账号不能为空！");
        }
        if (admin.getPwd().isEmpty()){
            return Result.error("注册失败，密码不能为空！");
        }

        // 排查账号是否存在
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", admin.getAccount());
        long count = adminMapper.selectCount(queryWrapper);
        if(count > 0){
            return Result.error("注册失败，该账号已存在！");
        }

        // 加密管理员输入的密码后再存入数据库
        String plainPassword = admin.getPwd();
        String hashedPassword = DigestUtils.md5Hex(plainPassword);
        admin.setPwd(hashedPassword);

        int i = adminMapper.insert(admin);

        if(i > 0){
            return Result.success();
        }else {
            return Result.error("账号创建失败！");
        }
    }

    @PostMapping("/login")
    public Result<String> login(@RequestBody Admin admin){
        // 加密用户输入的密码后与数据库的数据比对
        String plainPassword = admin.getPwd();
        String hashedPassword = DigestUtils.md5Hex(plainPassword);
        admin.setPwd(hashedPassword);

        // 条件查询
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", admin.getAccount()).eq("pwd", admin.getPwd());
        long count = adminMapper.selectCount(queryWrapper);

        if (count > 0) {
            // 生成jwt令牌,并返给前端
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", admin.getId());
            claims.put("account", admin.getAccount());
            String token = JwtUtil.getToken(claims);

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

    @PostMapping("/add_flight")
    public Result<String> addFlight(@RequestBody AddFlight addFlight){

        // 判断加入航班数据是否合理
        if(addFlight.dataOk().getCode() == 1){
            // 添加的航班信息有误
            return addFlight.dataOk();
        }

        int i = flightMapper.insert(addFlight.getFlight());
        if(i > 0){
            return Result.success();
        }else {
            return Result.error("添加失败");
        }

    }

    @PutMapping("/update_flight")
    public Result<String> updateFlight(@RequestBody UpdateFlight updateFlight){
        // 判断加入航班数据是否合理
        if(updateFlight.dataOk().getCode() == 1){
            // 添加的航班信息有误
            return updateFlight.dataOk();
        }

//        Collection<Flight> entityList = Collections.singletonList(updateFlight.getFlight());
//        List<BatchResult> batchResults = flightMapper.updateById(entityList);
//        BatchResult batchResult = batchResults.isEmpty() ? null : batchResults.get(0);

        int count = flightMapper.updateById(updateFlight.getFlight());

        if (count > 0){
            return Result.success();
        }else {
            return Result.error("更新失败！");
        }

//        // 处理更新结果
//        if (batchResult != null) {
//            System.out.println("Affected Rows: " + Arrays.toString(batchResult.getUpdateCounts()));
//            return Result.success();
//        } else {
//            System.out.println("Update failed.");
//            return Result.error("Update failed.");
//        }
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

    @PostMapping("/query_date")
    public Result<String> queryDate(@RequestBody QueryDate queryDate){

        LocalDateTime dateTime = queryDate.stringToDate();

        // 获取 fromTime 的日期部分
        LocalDate date = dateTime.toLocalDate();
        LocalDateTime startOfDay = date.atStartOfDay();

        // 条件查询
        QueryWrapper<Flight> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .ge("from_time", startOfDay)
                .lt("from_time", startOfDay.plusDays(1));

        List<Flight> list = flightMapper.selectList(queryWrapper);
        return Result.success(gson.toJson(list));
    }

    @GetMapping("/get_all_ticket_record")
    public Result<String> getAllTicketRecord(){
        List<Ticketrecord> list = ticketrecordService.selectAllTicketRecord();
        return Result.success(gson.toJson(list));
    }

    @PostMapping("/get_ticket_record_date")
    public Result<String> getTicketRecordDate(@RequestBody TicketRecordDate time){
        List<Ticketrecord> list = ticketrecordService.selectTicketRecordDate(time.stringToDate());
        return Result.success(gson.toJson(list));
    }

    @PostMapping("/get_ticket_record_any")
    public Result<String> getTicketRecordAny(@RequestBody TicketAny ticketAny){
        List<Ticketrecord> list = ticketrecordService.selectTicketRecordAny(ticketAny);
        return Result.success(gson.toJson(list));
    }

    @PatchMapping("/update_admin")
    public Result<String> updateAdmin(@RequestBody Map<String, String> params){
//        // 加密用户输入的密码后与数据库的数据比对
//        String plainPassword = admin.getPwd();
//        String hashedPassword = DigestUtils.md5Hex(plainPassword);
//        admin.setPwd(hashedPassword);
//
//        // 更新用户信息
//        Collection<Admin> entityList = Collections.singletonList(admin);
//        List<BatchResult> batchResults = adminMapper.updateById(entityList);
//        BatchResult batchResult = batchResults.isEmpty() ? null : batchResults.get(0);;
//
//        // 处理更新结果
//        if (batchResult != null) {
//            System.out.println("Affected Rows: " + Arrays.toString(batchResult.getUpdateCounts()));
//            return Result.success();
//        } else {
//            System.out.println("Update failed.");
//            return Result.error("Update failed.");
//        }

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

        // 获取相应的账号
        Map<String, Object> map = ThreadLocalUtil.get();
        String account = (String) map.get("account");

//        System.out.println("map is : " + map);
//        System.out.println("account : " + map.get("account"));


        // 根据token中的用户id从数据库中获取到的相应的用户
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", account);

        Admin loginAdmin = adminMapper.selectOne(queryWrapper);

        // 对用户写入的oldPwd进行加密
        String hashedPassword = DigestUtils.md5Hex(oldPwd);

        if (!loginAdmin.getPwd().equals(hashedPassword)){
            return Result.error("原密码填写不正确");
        }

        // newPwd和rePwd是否一样
        if (!rePwd.equals(newPwd)){
            return Result.error("两次填写的新密码不一致");
        }

        //2.完成密码更新
        String hashedPwd = DigestUtils.md5Hex(newPwd);
        loginAdmin.setPwd(hashedPwd);

        int i = adminMapper.update(loginAdmin, queryWrapper);

        if (i > 0){
            return Result.success();
        }else {
            return Result.error("密码更新失败！");
        }

    }

    @DeleteMapping("/deleteFlight/{f_id}")
    public Result<String> deleteFlight(@PathVariable("f_id") Long fId){
        if (flightMapper.selectById(fId) == null){
            return Result.error("不用删除，没有该趟航班！");
        }

        int count = flightMapper.deleteById(fId);
        if (count > 0){
            return Result.success();
        }else {
            return Result.error("删除失败！");
        }
    }

}
