package com.nova.admin.interceptor;

import com.nova.admin.utils.CurrentHolder;
import com.nova.admin.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {

    private final JwtUtils jwtUtils;

    public TokenInterceptor(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // ERROR/ASYNC 转发不应再做鉴权，否则会把业务异常伪装成 401
        if (request.getDispatcherType() != DispatcherType.REQUEST) {
            return true;
        }

        String token = request.getHeader("token");
        if (token == null || token.isEmpty()) {
            log.info("token为空，响应401，uri={}", request.getRequestURI());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        try {
            Claims claims = jwtUtils.parseToken(token);
            Integer id = (Integer) claims.get("id");
            CurrentHolder.setCurrentId(id);
            log.info("当前用户id为：{}", id);
        } catch (Exception e) {
            log.info("令牌非法，响应401");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        log.info("token合法");
        return true;
    }
}
