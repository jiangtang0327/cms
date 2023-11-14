package com.pakho.cms.web.interceptor;

import com.pakho.cms.exception.ServiceException;
import com.pakho.cms.util.JwtUtil;
import com.pakho.cms.util.ResultCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class JwtInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 当请求方式预检请求时，不需要进行token验证
        String method = request.getMethod();
        if (method.equals("OPTIONS")) {
            return true;//通过拦截
        }

        // 1.获取请求头信息token
        String token = request.getHeader("token");

        if (token == null) {
            throw new RuntimeException("无token，请重新登录");
        }
        try {
            String id = JwtUtil.getUserId(token);
            request.setAttribute("userId",id);
        } catch (ExpiredJwtException e) {
            //登录到期
            throw new RuntimeException("登录到期");
        } catch (MalformedJwtException e) {
            //令牌失效
            throw new RuntimeException("令牌失效");
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(ResultCode.SYSTEM_INNER_ERROR);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}