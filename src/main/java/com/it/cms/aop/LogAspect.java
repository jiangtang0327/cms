package com.it.cms.aop;

import com.google.gson.Gson;
import com.it.cms.bean.Ip;
import com.it.cms.util.IPUtils;
import com.it.cms.bean.Log;
import com.it.cms.exception.ServiceException;
import com.it.cms.mapper.LogMapper;
import com.it.cms.mapper.UserMapper;
import com.it.cms.util.JwtUtil;
import com.it.cms.util.ResultCode;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Aspect
@Component
public class LogAspect {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private LogMapper logMapper;
    @Autowired
    private Gson gson;

    private Long startTime = 0L;
    private Long endTime = 0L;

    @Pointcut("@annotation(com.it.cms.aop.Logging)")
    public void pt() {
    }

    @Around("pt()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Log log = handleBefore(joinPoint);
        Object proceed = joinPoint.proceed();
        handleAfter(log, proceed);

        return proceed;
    }

    public Log handleBefore(ProceedingJoinPoint joinPoint) {
        // 开始时间
        startTime = System.currentTimeMillis();

        // 创建日志对象
        Log log = new Log();

        //获取请求信息
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        //获取用户名
        String token = request.getHeader("Authorization");
        token = token.replace("Bearer ", "");
        if (!StringUtils.hasText(token)) { // 如果token为空
            throw new ServiceException(ResultCode.FAILURE); // 抛出异常
        }
        String userId = JwtUtil.getUserId(token);
        String username = userMapper.selectById(userId).getUsername();
        log.setUsername(username);

        //获取接口描述信息
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Logging logging = methodSignature.getMethod().getAnnotation(Logging.class);
        String value = logging.value();
        log.setBusinessName(value);

        //请求的地址
        String requestURL = request.getRequestURL().toString();
        log.setRequestUrl(requestURL);

        //请求的方法
        String method = request.getMethod();
        log.setRequestMethod(method);

        //获取ip
        Ip ip = IPUtils.getIP(request);
        String ipStr = ip.getIp();
        log.setIp(ipStr);

        //获取ip来源
        String addr = ip.getAddr();
        log.setSource(addr);

        //获取请求时间
        log.setCreateTime(new Date());

        //获取请求参数
        String s = gson.toJson(joinPoint.getArgs());
        log.setParamsJson(s);

        return log;
    }

    public void handleAfter(Log log, Object proceed) {
        // 响应参数
        String s = gson.toJson(proceed);
        log.setResultJson(s);

        // 结束时间
        endTime = System.currentTimeMillis();

        // 接口耗时
        log.setSpendTime((endTime - startTime));

        // 保存日志
        logMapper.insert(log);
    }

}
