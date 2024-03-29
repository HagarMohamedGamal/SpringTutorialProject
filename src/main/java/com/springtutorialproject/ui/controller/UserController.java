package com.springtutorialproject.ui.controller;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springtutorialproject.service.AddressService;
import com.springtutorialproject.service.UserService;
import com.springtutorialproject.shared.dto.AddressDto;
import com.springtutorialproject.shared.dto.UserDto;
import com.springtutorialproject.ui.model.request.RequestOperationName;
import com.springtutorialproject.ui.model.request.UserDetailsRequestModel;
import com.springtutorialproject.ui.model.response.AddressRest;
import com.springtutorialproject.ui.model.response.OperationStatusModel;
import com.springtutorialproject.ui.model.response.RequestOperationStatus;
import com.springtutorialproject.ui.model.response.UserRest;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController // to be able to accept http requests
@RequestMapping("users") // http://localhost:8080/users
//@CrossOrigin(origins = "*"/* {"http://localhost:8083"} */)
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	AddressService addressService;

	@ApiImplicitParams({
		@ApiImplicitParam(name = "authorization", paramType = "header", value = "Bearer JWTtoken")
	})
	@GetMapping
	/* @CrossOrigin(origins = {"http://localhost:8083"}) */
	public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limit", defaultValue = "25") int limit) {
		if (page > 0)
			page -= 1;

		List<UserDto> userDtoList = userService.getUsers(page, limit);

		List<UserRest> returnValueList = new ArrayList<>();
		for (UserDto userDto : userDtoList) {
			UserRest returnValue = new UserRest();
			BeanUtils.copyProperties(userDto, returnValue);
			returnValueList.add(returnValue);
		}
		return returnValueList;
	}

	@ApiOperation(value = "Get User Details Web Service Endpoint",
			notes = "This web service endpoint returns User Details. User's public user id in URL path. For example : /users/ufh37rnju478")
	@GetMapping(path = "/{userId}")
	public UserRest getUser(@PathVariable String userId) {

		UserDto userDto = userService.findUserByUserId(userId);

		ModelMapper modelMapper = new ModelMapper();
		UserRest returnValue = modelMapper.map(userDto, UserRest.class);

		return returnValue;
	}

	@PostMapping
	public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) {

		ModelMapper modelMapper = new ModelMapper();
		UserDto userDto = modelMapper.map(userDetails, UserDto.class);

		userDto = userService.createUser(userDto);

		UserRest returnValue = modelMapper.map(userDto, UserRest.class);

		return returnValue;
	}

	@PutMapping(path = "/{userId}")
	public UserRest updateUser(@PathVariable String userId, @RequestBody UserDetailsRequestModel userDetails) {
		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userDetails, userDto);

		userDto = userService.updateUser(userId, userDto);

		UserRest returnValue = new UserRest();
		BeanUtils.copyProperties(userDto, returnValue);

		return returnValue;
	}

	@DeleteMapping(path = "/{userId}")
	public OperationStatusModel deleteUser(@PathVariable String userId) {
		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setOperationName(RequestOperationName.DELETE.name());
		userService.deleteUser(userId);
		returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return returnValue;
	}

	@GetMapping(path = "/{userId}/addresses")
	public CollectionModel<AddressRest> getUserAddresses(@PathVariable String userId) {

		List<AddressDto> addressDtoList = addressService.getAddressesByUserId(userId);

		Type listType = new TypeToken<List<AddressRest>>() {
		}.getType();
		ModelMapper modelMapper = new ModelMapper();
		List<AddressRest> returnValue = modelMapper.map(addressDtoList, listType);
		for (AddressRest addressRest : returnValue) {
			Link userAddress = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
					.getUserAddresse(userId, addressRest.getAddressId())).withSelfRel();
			addressRest.add(userAddress);
		}
		Link user = WebMvcLinkBuilder.linkTo(UserController.class).slash("users").slash(userId).withRel("user");
		Link userAddresses = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserAddresses(userId)).withSelfRel();
		return CollectionModel.of(returnValue, user, userAddresses);
	}

	@GetMapping(path = "/{userId}/addresses/{addressId}")
	public EntityModel<AddressRest> getUserAddresse(@PathVariable String userId, @PathVariable String addressId) {

		AddressDto addressDto = addressService.getAddress(addressId);

		ModelMapper modelMapper = new ModelMapper();
		AddressRest returnValue = modelMapper.map(addressDto, AddressRest.class);

		Link user = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUser(userId))
				.withRel("user");
//		returnValue.add(user);
		Link userAddresses = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserAddresses(userId))
				.withRel("userAddresses");
//		returnValue.add(userAddresses);
		Link self = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserAddresse(userId, addressId))
				.withSelfRel();
//		returnValue.add(self);
		return EntityModel.of(returnValue, Arrays.asList(user, userAddresses, self));
//		return returnValue;
	}

}
