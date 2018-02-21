package com.syf.analyze;

import com.syf.analyze.component.CustomMultipartResolver;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.MultipartResolver;

@SpringBootApplication
public class WebApplication {

    @Bean(name = "multipartResolver")
    public MultipartResolver multipartResolver() {
        CustomMultipartResolver customMultipartResolver = new CustomMultipartResolver();
        return customMultipartResolver;
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(WebApplication.class).run(args);
    }
}


