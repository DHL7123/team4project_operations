package com.evo.evoproject;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.evo.evoproject.repository.product") //MyBatis 매퍼 인터페이스 패키지 경로
public class EvoprojectApplication {

	public static void main(String[] args) {
		SpringApplication.run(EvoprojectApplication.class, args);
	}

}
