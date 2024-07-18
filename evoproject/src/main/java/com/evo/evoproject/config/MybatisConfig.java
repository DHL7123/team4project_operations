package com.evo.evoproject.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@MapperScan("com.evo.evoproject.Mapper")//MyBatis 매퍼 인터페이스 패키지 경로
public class MybatisConfig {

}
