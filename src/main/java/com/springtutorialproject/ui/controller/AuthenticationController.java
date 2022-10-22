package com.springtutorialproject.ui.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.springtutorialproject.ui.model.request.UserLoginRequestModel;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;

// It's only used in swagger to add documentation to swagger
@RestController
public class AuthenticationController {

	@ApiOperation("User Login")//to change login URL description
	@ApiResponses({
		@ApiResponse(code = 200, 
				message = "Response Headers",
				responseHeaders = {
						@ResponseHeader(name = "authorization",
								description = "Bearer <JWT value here>",
								response = String.class),
						@ResponseHeader(name = "userId",
						description = "<Public User Id value here>",
						response = String.class)
				})
	})
	@PostMapping("/users/login")
	public void fakeLogin(@RequestBody UserLoginRequestModel userLoginRequestModel) {
//		It should not be called, because Spring Security will catch a request send to a login,
//		and it will override it and it will use its own web service to respond
		throw new IllegalStateException("This method should not be called. This method is implemented by Spring Security");
	}
	
}
