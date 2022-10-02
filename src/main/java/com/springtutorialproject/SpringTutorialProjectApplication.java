package com.springtutorialproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class SpringTutorialProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringTutorialProjectApplication.class, args);
	}
	
	@Bean
	BCryptPasswordEncoder getBCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	SpringApplicationContext getSpringApplicationContext() {
		return new SpringApplicationContext();
	}

}
