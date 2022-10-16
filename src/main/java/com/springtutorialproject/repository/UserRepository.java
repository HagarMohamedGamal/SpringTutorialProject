package com.springtutorialproject.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.springtutorialproject.entities.UserEntity;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {

	UserEntity findByEmail(String email);

	UserEntity findByUserId(String userId);

	@Query(value = "select * from Users u where u.email_verification_token = 'false'", countQuery = "select count(*) from Users u where u.email_verification_token = 'false'", nativeQuery = true)
	Page<UserEntity> findAllusersWithNonConfirmedEmail(Pageable pageableRequest);

	@Query(value = "select * from Users u where u.first_name = ?1", nativeQuery = true)
	List<UserEntity> findUserByFirstName(String firstName);

	@Query(value = "select * from Users u where u.last_name = :lastName", nativeQuery = true)
	List<UserEntity> findUserByLastName(@Param("lastName") String lastName);

	@Query(value = "select * from Users u where u.first_name LIKE %:keyword% or u.last_name LIKE %:keyword%", nativeQuery = true)
	List<UserEntity> findUserByKeyword(@Param("keyword") String keyword);

	@Query(value = "select u.first_name, u.last_name from Users u where u.first_name LIKE %:keyword% or u.last_name LIKE %:keyword%", nativeQuery = true)
	List<Object[]> findUserFirstNameAndLastNameByKeyword(@Param("keyword") String keyword);

}
