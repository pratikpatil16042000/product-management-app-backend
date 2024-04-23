package com.pratik.backendproductapp.config;

import java.util.ArrayList;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pratik.backendproductapp.contants.ProductConstants;
import com.pratik.backendproductapp.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerUsersDetailsService implements UserDetailsService{
	
	@Autowired
	UserRepository userRepo;
	
	private com.pratik.backendproductapp.entity.User userDetail;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		log.info("Inside loadUserByUsername {} ", email);
		userDetail = userRepo.findByEmail(email);
		if(!Objects.isNull(userDetail)) {
			return new User(userDetail.getEmail(),userDetail.getPassword(),new ArrayList<>());
		}
		else {
			throw new UsernameNotFoundException(ProductConstants.USER_NOT_FOUND);
		}
	}
	
	public com.pratik.backendproductapp.entity.User getUserDetail(){
		return userDetail;
	}

}
