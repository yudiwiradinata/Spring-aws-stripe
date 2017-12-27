package com.yudi.config;

import com.yudi.backend.service.EmailService;
import com.yudi.backend.service.MockEmailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by Yudi on 27/12/2017.
 */
@Configuration
@Profile("dev")
@PropertySource("file:///D:\\Workspace\\Spring-aws-stripe\\config\\application-dev.properties")
public class DevelopmentConfig {

    @Bean
    public EmailService emailService(){
        return new MockEmailService();
    }
}
