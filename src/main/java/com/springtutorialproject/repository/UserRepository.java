package com.springtutorialproject.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

	@Transactional
	@Modifying
	@Query(value = "update Users u set u.email_verification_token = :emailVerificationStatus where u.user_id = :userId", nativeQuery = true)
	void updateUserEmailVerificationStatus(@Param("emailVerificationStatus") boolean emailVerificationStatus,
			@Param("userId") String userId);

	@Query(value = "select user from UserEntity user where user.userId = :userId")
	UserEntity findUserEntityByUserId(@Param("userId") String userId);

	@Query(value = "select user.firstName, user.lastName from UserEntity user where user.userId = :userId")
	List<Object[]> findUserFirstNameAndLastNameByUserId(@Param("userId") String userId);

	@Transactional
	@Modifying
	@Query(value = "update UserEntity u set u.emailVerificationToken = :emailVerificationStatus where u.userId = :userId")
	void updateUserEntityEmailVerificationStatus(@Param("emailVerificationStatus") boolean emailVerificationStatus,
			@Param("userId") String userId);



}
