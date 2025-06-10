package org.xiaoshuyui.auth.config;

import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.xiaoshuyui.auth.util.JwtUtil;
import org.xiaoshuyui.common.UserDefinedException;

@Component
@Slf4j
public class JwtAuthFilter implements Filter {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws ServletException, IOException {
    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse res = (HttpServletResponse) response;
    String auth = req.getHeader("Authorization");

    if (auth != null && auth.startsWith("Bearer ")) {
      String token = auth.substring(7);
      try {
        Claims claims = JwtUtil.parseToken(token);
        log.info("claims: {}", claims);
        // 可以将 claims 存到上下文 ThreadLocal 里
        request.setAttribute("claims", claims);
      } catch (Exception e) {
//        throw new RuntimeException("Token 无效或过期");
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        res.setContentType("application/json;charset=utf-8");
        res.getWriter().write("{\"code\":401,\"message\":\"Token 无效或过期\"}");
      }
    }else{
      // dont handle when debug
//      res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//      res.setContentType("application/json;charset=utf-8");
//      res.getWriter().write("{\"code\":401,\"message\":\"请先登录\"}");
//      return;
    }

    chain.doFilter(request, response);
  }
}
