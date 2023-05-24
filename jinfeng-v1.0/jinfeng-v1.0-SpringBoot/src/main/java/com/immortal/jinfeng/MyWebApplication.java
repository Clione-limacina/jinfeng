package com.immortal.jinfeng;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ServletComponentScan//启用servlet、filter等
@MapperScan(basePackages = "com.immortal.jinfeng.access")
//@PropertySource("classpath:myconfig.properties")
@EnableTransactionManagement//声明式事务管理
//@EnableScheduling//定时任务
public class MyWebApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {

        SpringApplication.run(MyWebApplication.class, args);


    }
}
