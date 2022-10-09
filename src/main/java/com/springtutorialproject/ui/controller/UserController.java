package com.springtutorialproject.ui.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springtutorialproject.service.UserService;
import com.springtutorialproject.shared.dto.UserDto;
import com.springtutorialproject.ui.model.request.RequestOperationName;
import com.springtutorialproject.ui.model.request.UserDetailsRequestModel;
import com.springtutorialproject.ui.model.response.OperationStatusModel;
import com.springtutorialproject.ui.model.response.RequestOperationStatus;
import com.springtutorialproject.ui.model.response.UserRest;

@RestController	//to be able to accept http requests
@RequestMapping("users")	//http://localhost:8080/users
public class UserController {
	
	@Autowired
	UserService userService;

	@GetMapping(path = "/{userId}")
	public UserRest getUser(@PathVariable String userId) {
		
		UserDto userDto = userService.findUserByUserId(userId);
		
		UserRest returnValue = new UserRest();
		BeanUtils.copyProperties(userDto, returnValue);
		return returnValue;
	}

	@PostMapping
	public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) {
		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userDetails, userDto);
		
		userDto = userService.createUser(userDto);
		
		UserRest returnValue = new UserRest();
		BeanUtils.copyProperties(userDto, returnValue);
		
		return returnValue;
	}

	@PutMapping
	public String updateUser() {
		return "update user was called";
	}

	@DeleteMapping(path = "/{userId}")
	public OperationStatusModel deleteUser(@PathVariable String userId) {
		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setOperationName(RequestOperationName.DELETE.name());
		userService.deleteUser(userId);
		returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return returnValue;
	}
	
}
