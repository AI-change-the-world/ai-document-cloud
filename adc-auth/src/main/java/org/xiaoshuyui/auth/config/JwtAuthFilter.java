package org.xiaoshuyui.auth.config;

import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import org.springframework.stereotype.Component;
import org.xiaoshuyui.auth.util.JwtUtil;

@Component
public class JwtAuthFilter implements Filter {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws ServletException, IOException {
    HttpServletRequest req = (HttpServletRequest) request;
    String auth = req.getHeader("Authorization");

    if (auth != null && auth.startsWith("Bearer ")) {
      String token = auth.substring(7);
      try {
        Claims claims = JwtUtil.parseToken(token);
        // 可以将 claims 存到上下文 ThreadLocal 里
        request.setAttribute("claims", claims);
      } catch (Exception e) {
        throw new RuntimeException("Token 无效或过期");
      }
    }

    chain.doFilter(request, response);
  }
}
