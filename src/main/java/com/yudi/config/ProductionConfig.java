package com.yudi.config;

import com.yudi.backend.service.EmailService;
import com.yudi.backend.service.SmtpEmailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by Yudi on 27/12/2017.
 */
@Configuration
@Profile("prod")
@PropertySource("file:///D:\\Workspace\\Spring-aws-stripe\\config\\application-prod.properties")
public class ProductionConfig {

    @Bean
    public EmailService emailService(){
        return new SmtpEmailService();
    }
}
