package com.syf.analyze;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
@EntityScan(value="com.syf.analyze.domain")
public class WebApplication {
    public static void main(String[] args){
        new SpringApplicationBuilder(WebApplication.class).run(args);
    }
}


