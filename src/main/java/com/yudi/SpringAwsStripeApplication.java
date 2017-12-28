package com.yudi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.yudi.backend.persistence.repositories")
public class SpringAwsStripeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringAwsStripeApplication.class, args);
	}
}
