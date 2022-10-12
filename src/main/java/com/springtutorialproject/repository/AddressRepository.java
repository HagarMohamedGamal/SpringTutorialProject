package com.springtutorialproject.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.springtutorialproject.entities.AddressEntity;
import com.springtutorialproject.entities.UserEntity;

@Repository
public interface AddressRepository extends CrudRepository<AddressEntity, Long> {

	List<AddressEntity> findByUserDetails(UserEntity userEntity);

}
