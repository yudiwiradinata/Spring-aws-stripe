package com.yudi.config;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by Yudi on 28/12/2017.
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.yudi.backend.persistence.repositories")
@EntityScan(basePackages = "com.yudi.backend.persistence.domain.backend")
@EnableTransactionManagement
@PropertySource("file:///D:\\Workspace\\Spring-aws-stripe\\config\\application-common.properties")
@PropertySource("file:///D:\\Workspace\\Spring-aws-stripe\\config\\stripe.properties")
public class ApplicationConfig {

    @Value("${aws.s3.profile}")
    private String awsProfileName;

    @Bean
    public AmazonS3 s3Client(){
        AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(new ProfileCredentialsProvider(awsProfileName))
                .withRegion(Regions.AP_SOUTHEAST_1).build();
        return s3;
    }
}
