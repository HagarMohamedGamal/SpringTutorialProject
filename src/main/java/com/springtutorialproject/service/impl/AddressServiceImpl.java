package com.springtutorialproject.service.impl;

import java.lang.reflect.Type;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springtutorialproject.entities.AddressEntity;
import com.springtutorialproject.entities.UserEntity;
import com.springtutorialproject.repository.AddressRepository;
import com.springtutorialproject.repository.UserRepository;
import com.springtutorialproject.service.AddressService;
import com.springtutorialproject.shared.dto.AddressDto;

@Service
public class AddressServiceImpl implements AddressService {
	@Autowired
	UserRepository userRepository;
	@Autowired
	AddressRepository addressRepository;

	@Override
	public List<AddressDto> getAddressesByUserId(String userId) {
		UserEntity userEntity = userRepository.findByUserId(userId);
		List<AddressEntity> addressEntityList = addressRepository.findByUserDetails(userEntity);

		Type listType = new TypeToken<List<AddressDto>>() {}.getType();
		ModelMapper modelMapper = new ModelMapper();
		List<AddressDto> returnValue  = modelMapper.map(addressEntityList, listType);
		return returnValue;
	}

}
