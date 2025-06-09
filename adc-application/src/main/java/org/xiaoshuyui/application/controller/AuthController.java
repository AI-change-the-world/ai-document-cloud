package org.xiaoshuyui.application.controller;

import jakarta.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.*;
import org.xiaoshuyui.auth.entity.AuthRequest;
import org.xiaoshuyui.auth.util.JwtUtil;
import org.xiaoshuyui.common.ApiResponse;
import org.xiaoshuyui.db.user.entity.User;
import org.xiaoshuyui.db.user.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

  @Resource private UserService userService;

  // 模拟用户名密码登录
  @PostMapping("/login")
  public Map<String, Object> login(@RequestBody AuthRequest request) {
    // 假设验证通过（真实环境中请调用数据库或 service 验证）
    if ("admin".equals(request.getUsername()) && "123456".equals(request.getPassword())) {
      Map<String, Object> claims = new HashMap<>();
      claims.put("username", request.getUsername());
      claims.put("role", "ADMIN");

      String token = JwtUtil.generateToken(claims);

      Map<String, Object> result = new HashMap<>();
      result.put("token", token);
      result.put("user", claims);
      return result;
    } else {
      throw new RuntimeException("用户名或密码错误");
    }
  }

  @GetMapping("/user-info/{id}")
  public ApiResponse getUserInfo(@PathVariable("id") Long id) {
    User user = userService.getUserById(id);
    return ApiResponse.success(user);
  }
}
