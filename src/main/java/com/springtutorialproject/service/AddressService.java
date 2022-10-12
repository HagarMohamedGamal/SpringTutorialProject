package com.springtutorialproject.service;

import java.util.List;

import com.springtutorialproject.shared.dto.AddressDto;

public interface AddressService {

	List<AddressDto> getAddressesByUserId(String userId);

}
