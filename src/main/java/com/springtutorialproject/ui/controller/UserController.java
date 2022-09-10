package com.springtutorialproject.ui.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springtutorialproject.shared.dto.UserDto;
import com.springtutorialproject.ui.model.request.UserDetailsRequestModel;
import com.springtutorialproject.ui.model.response.UserRest;

@RestController	//to be able to accept http requests
@RequestMapping("users")	//http://localhost:8080/users
public class UserController {

	@GetMapping
	public String getUser() {
		return "get user was called";
	}

	@PostMapping
	public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) {
		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userDetails, userDto);
		
//		userDto = userService.createUser();
		
		UserRest returnValue = new UserRest();
		BeanUtils.copyProperties(userDto, returnValue);
		
		return returnValue;
	}

	@PutMapping
	public String updateUser() {
		return "update user was called";
	}

	@DeleteMapping
	public String deleteUser() {
		return "delete user was called";
	}
	
}
