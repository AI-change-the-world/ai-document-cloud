package org.xiaoshuyui.application.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@Schema(name = "测试用的controller", description = "测试用的controller，不在生产环境中使用")
public class TestController {

  @GetMapping("/error")
  public String error() throws Exception {
    throw new Exception("测试异常");
  }
}
