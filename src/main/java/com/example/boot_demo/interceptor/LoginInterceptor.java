package com.example.boot_demo.interceptor;


import com.example.boot_demo.utils.JwtUtil;
import com.example.boot_demo.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{

        // 令牌验证
        String token = request.getHeader("Authorization");
        System.out.println("token = " + token);
        System.out.println("token = " + token);
        System.out.println("token = " + token);
        System.out.println("\n");

        // 过预检
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // 验证token
        try{
//            // 从redis中获取token
//            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
//            String redisToken = operations.get(token);
//            if (redisToken == null){
//                // token已经失效
//                System.out.println("redisToken == null!");
//                throw new RuntimeException();
//            }

            // 解析token
            Map<String, Object> claims = JwtUtil.parseToken(token);

            // 把业务数据存到ThreadLocal中
            ThreadLocalUtil.set(claims);

            // 解析成功，放行
            return true;

        } catch (Exception e){

            // Http相应码状态为401
            response.setStatus(401);

            // 解析失败，不放行
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 清空ThreadLocal中的数据
        ThreadLocalUtil.remove();
    }
}
