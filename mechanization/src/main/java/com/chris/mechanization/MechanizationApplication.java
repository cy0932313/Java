package com.chris.mechanization;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication


@MapperScan("com.chris.mechanization.dao")

public class MechanizationApplication {

	public static void main(String[] args) {
		SpringApplication.run(MechanizationApplication.class, args);
	}

}
