package com.springtutorialproject.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.springtutorialproject.shared.dto.UserDto;

public interface UserService extends UserDetailsService {
	
	UserDto createUser(UserDto userDto);
	UserDto getUser(String email);
	UserDto findUserByUserId(String userId);
	void deleteUser(String userId);
	UserDto updateUser(String userId, UserDto userDto);
	List<UserDto> getUsers(int page, int limit);

}
