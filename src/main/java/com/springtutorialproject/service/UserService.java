package com.springtutorialproject.service;

import org.springframework.stereotype.Service;

import com.springtutorialproject.shared.dto.UserDto;

@Service
public interface UserService {
	
	UserDto createUser(UserDto userDto);

}
