package org.xiaoshuyui.application.controller;

import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.web.bind.annotation.*;
import org.xiaoshuyui.auth.entity.AuthRequest;
import org.xiaoshuyui.auth.util.JwtUtil;
import org.xiaoshuyui.common.ApiResponse;
import org.xiaoshuyui.common.UserDefinedException;
import org.xiaoshuyui.db.user.entity.User;
import org.xiaoshuyui.db.user.service.UserService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Resource
    private UserService userService;

    @Data
    static class UserToken {
        String token;
        Long userId;
        String role;
    }

    // 模拟用户名密码登录
    @PostMapping("/login")
    public ApiResponse<UserToken> login(@RequestBody AuthRequest request) throws Exception {
        User user = userService.getUser(request.getUsername(), request.getPassword().toUpperCase());
        if (user == null) {
            throw new UserDefinedException("用户名或密码错误");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("username", request.getUsername());
        claims.put("userId", user.getId());
        UserToken result = new UserToken();
        if ("admin".equals(user.getUsername()) && (user.getId() == 1L)) {
            claims.put("role", "ADMIN");
            result.setRole("ADMIN");
        } else {
            claims.put("role", "USER");
            result.setRole("USER");
        }

        String token = JwtUtil.generateToken(claims);


        result.setToken(token);
        result.setUserId(user.getId());
        return ApiResponse.success(result);
    }

    @GetMapping("/user-info/{id}")
    public ApiResponse getUserInfo(@PathVariable("id") Long id) {
        User user = userService.getUserById(id);
        return ApiResponse.success(user);
    }
}
