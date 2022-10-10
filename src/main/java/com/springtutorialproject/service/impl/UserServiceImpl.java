package com.springtutorialproject.service.impl;

import java.util.ArrayList;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
			throw new RuntimeException(ErrorMessagesEnum.RECORD_ALREADY_EXISTS.getErrorMessage());

		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(userDto, userEntity);

		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));

		String publicUserId = utils.generateRandomString(30);
		userEntity.setUserId(publicUserId);

		UserEntity storedUserDetails = userRepository.save(userEntity);

		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(storedUserDetails, returnValue);

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
			throw new UsernameNotFoundException(userId);

		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userEntity, userDto);
		return userDto;
	}

	@Override
	public void deleteUser(String userId) {
		UserEntity userEntity = userRepository.findByUserId(userId);
		if (userEntity == null)
			throw new UsernameNotFoundException(userId);

		userRepository.delete(userEntity);
	}

	@Override
	public UserDto updateUser(String userId, UserDto userDto) {
		UserEntity userEntity = userRepository.findByUserId(userId);
		if (userEntity == null)
			throw new UsernameNotFoundException(userId);

		if (userDto.getFirstName()!=null && !userDto.getFirstName().isEmpty())
			userEntity.setFirstName(userDto.getFirstName());

		if (userDto.getLastName()!=null && !userDto.getLastName().isEmpty())
			userEntity.setLastName(userDto.getLastName());

		userEntity = userRepository.save(userEntity);
		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(userEntity, returnValue);
		return returnValue;
	}

}
