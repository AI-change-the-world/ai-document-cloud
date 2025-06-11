package org.xiaoshuyui.application.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiaoshuyui.common.ApiResponse;
import org.xiaoshuyui.db.global.PageRequest;
import org.xiaoshuyui.db.user.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/list/summary")
    public ApiResponse getUserSummary() {
        return ApiResponse.success(userService.getUserSummary());
    }

    @PostMapping("/list")
    public ApiResponse getUsers(@RequestBody PageRequest pageRequest) {
        log.info("getUsers: pageId={}, pageSize={}", pageRequest.getPageId(), pageRequest.getPageSize());
        return ApiResponse.success(
                userService.getAllUsers(pageRequest.getPageId(), pageRequest.getPageSize(), null));
    }
}
