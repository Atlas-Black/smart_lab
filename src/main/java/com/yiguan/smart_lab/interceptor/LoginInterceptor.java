package com.yiguan.smart_lab.interceptor;

import com.yiguan.smart_lab.context.UserContext;
import com.yiguan.smart_lab.exception.BusinessException;
import com.yiguan.smart_lab.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HttpServletBean;

public class LoginInterceptor implements HandlerInterceptor{

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        String token = request.getHeader("Authorization");
        if(token == null || token.isBlank()){
            throw new BusinessException("未登录");
        }

        try{
            Long userId = JwtUtils.parseToken(token);
            UserContext.setUserId(userId);
            return true;
        } catch(Exception e){
            throw new BusinessException("登录已失效");
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex){
        UserContext.clear();
    }
}
