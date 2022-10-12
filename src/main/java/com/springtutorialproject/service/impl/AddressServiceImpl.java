package com.springtutorialproject.service.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springtutorialproject.entities.AddressEntity;
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
//		Way1
//		UserEntity userEntity = userRepository.findByUserId(userId);
//		List<AddressEntity> addressEntityList = addressRepository.findByUserDetails(userEntity);
//		Way2
		List<AddressEntity> addressEntityList = addressRepository.findByUserDetailsUserId(userId);
		if (addressEntityList.isEmpty())
			return new ArrayList<>();
		Type listType = new TypeToken<List<AddressDto>>() {
		}.getType();
		ModelMapper modelMapper = new ModelMapper();
		List<AddressDto> returnValue = modelMapper.map(addressEntityList, listType);
		return returnValue;
	}

	@Override
	public AddressDto getAddress(String addressId) {
		AddressEntity addressEntity = addressRepository.findByAddressId(addressId);
		if (addressEntity == null)
			return null;
		ModelMapper modelMapper = new ModelMapper();
		AddressDto returnValue = modelMapper.map(addressEntity, AddressDto.class);
		return returnValue;
	}

}
