package com.springtutorialproject.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.springtutorialproject.entities.UserEntity;
import com.springtutorialproject.exceptions.UserServiceException;
import com.springtutorialproject.repository.UserRepository;
import com.springtutorialproject.service.UserService;
import com.springtutorialproject.shared.Utils;
import com.springtutorialproject.shared.dto.AddressDto;
import com.springtutorialproject.shared.dto.UserDto;
import com.springtutorialproject.ui.model.response.ErrorMessagesEnum;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	Utils utils;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public UserDto createUser(UserDto userDto) {
		if(userDto.getEmail() == null || userDto.getEmail().isEmpty()
				|| userDto.getFirstName() == null || userDto.getFirstName().isEmpty()
						|| userDto.getLastName() == null || userDto.getLastName().isEmpty()
								|| userDto.getPassword() == null || userDto.getPassword().isEmpty()) {
			throw new UserServiceException(ErrorMessagesEnum.MISSING_REQUIRED_FIELD.getErrorMessage());
		}
		if (userRepository.findByEmail(userDto.getEmail()) != null)
			throw new UserServiceException(ErrorMessagesEnum.RECORD_ALREADY_EXISTS.getErrorMessage());

		for(int i=0; i<userDto.getAddresses().size(); i++) {
			AddressDto addressDto = userDto.getAddresses().get(i);
			addressDto.setUserDetails(userDto);
			addressDto.setAddressId(utils.generateAddressId(30));
			userDto.getAddresses().set(i, addressDto);
		}
		
		ModelMapper modelMapper = new ModelMapper();
		UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);

		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));

		userEntity.setUserId(utils.generateUserId(30));

		UserEntity storedUserDetails = userRepository.save(userEntity);

		UserDto returnValue = modelMapper.map(storedUserDetails, UserDto.class);

		return returnValue;
	}

	@Override
	public UserDto getUser(String email) {
		UserEntity userEntity = userRepository.findByEmail(email);

		if (userEntity == null)
			throw new UsernameNotFoundException(email);

		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(userEntity, returnValue);
		return returnValue;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserEntity userEntity = userRepository.findByEmail(email);

		if (userEntity == null)
			throw new UsernameNotFoundException(email);

		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
	}

	@Override
	public UserDto findUserByUserId(String userId) {
		UserEntity userEntity = userRepository.findByUserId(userId);
		if (userEntity == null)
			throw new UserServiceException(ErrorMessagesEnum.NO_RECORD_FOUND.getErrorMessage());

		ModelMapper modelMapper = new ModelMapper();
		UserDto returnValue = modelMapper.map(userEntity, UserDto.class);
		return returnValue;
	}

	@Override
	public void deleteUser(String userId) {
		UserEntity userEntity = userRepository.findByUserId(userId);
		if (userEntity == null)
			throw new UserServiceException(ErrorMessagesEnum.NO_RECORD_FOUND.getErrorMessage());

		userRepository.delete(userEntity);
	}

	@Override
	public UserDto updateUser(String userId, UserDto userDto) {
		UserEntity userEntity = userRepository.findByUserId(userId);
		if (userEntity == null)
			throw new UserServiceException(ErrorMessagesEnum.NO_RECORD_FOUND.getErrorMessage());

		if (userDto.getFirstName()!=null && !userDto.getFirstName().isEmpty())
			userEntity.setFirstName(userDto.getFirstName());

		if (userDto.getLastName()!=null && !userDto.getLastName().isEmpty())
			userEntity.setLastName(userDto.getLastName());

		userEntity = userRepository.save(userEntity);
		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(userEntity, returnValue);
		return returnValue;
	}

	@Override
	public List<UserDto> getUsers(int page, int limit) {
		Sort sort = Sort.by("id").descending();
		Pageable pageable = PageRequest.of(page, limit, sort);
		Page<UserEntity> userEntityPage = userRepository.findAll(pageable);
		List<UserEntity> userEntityList = userEntityPage.getContent();
		
		List<UserDto> returnValueList = new ArrayList<>();
		for(UserEntity userEntity : userEntityList) {
			UserDto returnValue = new UserDto();
			BeanUtils.copyProperties(userEntity, returnValue);
			returnValueList.add(returnValue);
		}
		return returnValueList;
	}

}
