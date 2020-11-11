package com.qzhao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
// 扫描 mybatis 通用 mapper 所在的包
@MapperScan(basePackages = "com.qzhao.mapper")
@ComponentScan(basePackages = {"com.qzhao", "org.n3r.idworker"})
public class FoodieApplication {
    public static void main(String[] args) {
        SpringApplication.run(FoodieApplication.class,args);
    }
}