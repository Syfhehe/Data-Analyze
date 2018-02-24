package com.syf.analyze.config;

import com.syf.analyze.spring.CustomMultipartResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.MultipartResolver;

public class FileUploadConfiguration {

    @Bean(name = "multipartResolver")
    public MultipartResolver multipartResolver() {
        CustomMultipartResolver customMultipartResolver = new CustomMultipartResolver();

        //Set the maximum allowed size (in bytes) for each individual file.
        customMultipartResolver.setMaxUploadSizePerFile(5242880);

        return customMultipartResolver;
    }

}
