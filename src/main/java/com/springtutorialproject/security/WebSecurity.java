package com.springtutorialproject.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
	 * UthenticationManager object is used to configure which service class in our
	 * app will be responsible to load user details from database And when user
	 * login, Spring will use this class to see if our database have a user with the
	 * provided credentials
	 */
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder = http
				.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
		AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
		http.authenticationManager(authenticationManager);
//		AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManagerBuilder.class).getOrBuild();
		http
		.cors()
		.and()
		.csrf().disable()
		.authorizeRequests()
		.antMatchers(HttpMethod.POST, SecurityConstants.SING_UP_URL)
		.permitAll()
		.antMatchers("/v2/api-docs", "/configuration/**", "/swagger*/**", "/webjars/**")
		.permitAll()
		.anyRequest().authenticated()
		.and()
		.addFilter(getAuthenticationFilter(authenticationManager))
		.addFilter(new AuthorizationFilter(authenticationManager))
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		return http.build();
	}

	public AuthenticationFilter getAuthenticationFilter(AuthenticationManager authenticationManager) {
		AuthenticationFilter filter = new AuthenticationFilter(authenticationManager);
		filter.setFilterProcessesUrl("/users/login");
		return filter;
	}
	
	@Bean
	public CorsConfiguration  corsConfiguration() {
		final CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("*"));//Or add list of origins
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));//Or Arrays.asList("*")
		configuration.setAllowCredentials(true);//It could be cookies or authorization headers or ssl client certificates
		configuration.setAllowedHeaders(Arrays.asList("*"));//Or Arrays.asList("Authorization", "Cache-Control", "Content-Type")
		
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();//uses URL path patterns to select the CorsConfiguration for a request
		source.registerCorsConfiguration("/**", configuration); //ot any specific path
		return configuration;
	}

//
//	@Bean
//	public WebSecurityCustomizer webSecurityCustomizer() {
//		return (web) -> {
//		};
//	}

}
