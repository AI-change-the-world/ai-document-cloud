package org.xiaoshuyui.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  // 捕获所有未处理的异常
  @ExceptionHandler(Exception.class)
  public ApiResponse handleException(Exception e) {
    log.error("服务器内部错误", e);
    return ApiResponse.fail(500, "服务器内部错误");
  }
}
