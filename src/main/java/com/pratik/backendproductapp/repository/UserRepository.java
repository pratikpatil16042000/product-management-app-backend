package com.pratik.backendproductapp.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pratik.backendproductapp.entity.User;
import com.pratik.backendproductapp.wrapper.UserWrapper;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
	User findByEmail(@Param("email") String email);
	
	List<UserWrapper> findAllUsers();
	
	List<String> findAllAdmins();
	
	@Transactional
	@Modifying
	Integer updateStatus(@Param("status") String status, @Param("id") Long id); 

}
