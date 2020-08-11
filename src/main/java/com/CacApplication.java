package com;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
@MapperScan({"com.jeecg.dao","com.scott.dao"})
public class CacApplication {

    public static void main(String[] args) {
        SpringApplication.run(CacApplication.class, args);
    }

}
