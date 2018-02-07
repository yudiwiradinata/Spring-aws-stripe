package com.yudi.config;

import com.yudi.backend.service.EmailService;
import com.yudi.backend.service.MockEmailService;
import org.h2.server.web.WebServlet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
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

    @Value("${stripe.test.private.key}")
    private String stripeDevKey;

    @Bean
    public EmailService emailService(){
        return new MockEmailService();
    }

    @Bean
    public ServletRegistrationBean h2Console(){
        ServletRegistrationBean bean =  new ServletRegistrationBean(new WebServlet());
        bean.addUrlMappings("/console/*");
        return bean;
    }

    @Bean
    public String stripeKey(){
        return stripeDevKey;
    }

}
