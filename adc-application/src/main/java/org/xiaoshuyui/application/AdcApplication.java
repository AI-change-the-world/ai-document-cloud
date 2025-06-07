package org.xiaoshuyui.application;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication(scanBasePackages = "org.xiaoshuyui")
@ConfigurationPropertiesScan
@MapperScan(value = "org.xiaoshuyui.db")
public class AdcApplication {
  public static void main(String[] args) {
    SpringApplication.run(AdcApplication.class, args);
  }
}
