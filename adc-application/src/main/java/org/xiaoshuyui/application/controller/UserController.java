package org.xiaoshuyui.application.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiaoshuyui.common.ApiResponse;
import org.xiaoshuyui.db.user.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource private UserService userService;

    @GetMapping("/list/summary")
    public ApiResponse getUserSummary() {
        return ApiResponse.success(userService.getUserSummary());
    }
}
