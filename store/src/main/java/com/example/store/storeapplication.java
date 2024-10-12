package com.example.store;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@MapperScan("com.example.store.mapper")
@EnableAsync
public class storeapplication {
	public static void main(String[] args) {
		SpringApplication.run(storeapplication.class, args);
	}
}
