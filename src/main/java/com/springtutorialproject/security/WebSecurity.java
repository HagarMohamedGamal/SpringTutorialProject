package com.springtutorialproject.security;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.springtutorialproject.service.UserService;

@EnableWebSecurity
public class WebSecurity {
	private final UserService userService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	WebSecurity(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userService = userService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}
/*
 * UthenticationManager object is used to configure which service class in our app will be responsible to load user details from database
 * And when user login, Spring will use this class to see if our database have a user with the provided credentials
 * */
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder = http
				.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder
		.userDetailsService(userService)
		.passwordEncoder(bCryptPasswordEncoder);
		AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
		http.authenticationManager(authenticationManager);
//		AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManagerBuilder.class).getOrBuild();
		http.csrf().disable().authorizeRequests().antMatchers(HttpMethod.POST, SecurityConstants.SING_UP_URL)
				.permitAll().anyRequest().authenticated().and()
				.addFilter(new AuthenticationFilter(authenticationManager));
		return http.build();
	}
//
//	@Bean
//	public WebSecurityCustomizer webSecurityCustomizer() {
//		return (web) -> {
//		};
//	}

}
