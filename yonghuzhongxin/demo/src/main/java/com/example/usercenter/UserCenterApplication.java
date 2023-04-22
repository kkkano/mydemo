package com.example.usercenter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.usercenter.mapper")//扫描 把你的增删改查注入到项目中
public class UserCenterApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserCenterApplication.class, args);
	}

}
