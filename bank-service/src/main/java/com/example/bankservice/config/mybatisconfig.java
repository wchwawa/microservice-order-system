package com.example.bankservice.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.example.bankservice.mapper")
public class mybatisconfig {
}
