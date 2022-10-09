package com.springtutorialproject.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.springtutorialproject.shared.dto.UserDto;

public interface UserService extends UserDetailsService {
	
	UserDto createUser(UserDto userDto);
	UserDto getUser(String email);
	UserDto findUserByUserId(String userId);
	void deleteUser(String userId);

}
