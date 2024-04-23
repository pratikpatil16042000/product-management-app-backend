package com.pratik.backendproductapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pratik.backendproductapp.config.CustomerUsersDetailsService;
import com.pratik.backendproductapp.contants.ProductConstants;
import com.pratik.backendproductapp.entity.User;
import com.pratik.backendproductapp.jwt.JwtService;
import com.pratik.backendproductapp.jwt.filter.JwtAuthFilter;
import com.pratik.backendproductapp.repository.UserRepository;
import com.pratik.backendproductapp.utils.EmailUtils;
import com.pratik.backendproductapp.utils.ProductUtils;
import com.pratik.backendproductapp.wrapper.UserWrapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	CustomerUsersDetailsService customerUsersDetailsService;
	
	@Autowired
	JwtService jwtService;
	
	@Autowired
	JwtAuthFilter jwtAuthFilter;
	
	@Autowired
	EmailUtils emailUtils;
	
	@Override
	public ResponseEntity<String> signUp(Map<String, String> requestMap) {
		log.info("inside signUp {}", requestMap);
		try {
		if(validateSignUpMap(requestMap)) {
			User user = userRepo.findByEmail(requestMap.get("email"));
			if(Objects.isNull(user)) {
				userRepo.save(getUserFromMap(requestMap));
				return ProductUtils.getResponseEntity(ProductConstants.USER_REGISTRATION_SUCCESSFUL, HttpStatus.OK);
			}
			else {
				//logic if user already exists
				return ProductUtils.getResponseEntity(ProductConstants.EMAIL_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
			}
		}
		else {
			return ProductUtils.getResponseEntity(ProductConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
		}
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return ProductUtils.getResponseEntity(ProductConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	//Method not available to client directly hence kept private
	private boolean validateSignUpMap(Map<String,String> requestMap) {
		if(requestMap.containsKey("name") && requestMap.containsKey("contactNumber") 
				&& requestMap.containsKey("email") && requestMap.containsKey("password")) {
			return true;
		}
		return false;
	}
	
	//Method not available to client directly hence kept private
	private User getUserFromMap(Map<String,String> requestMap) {
		User user =  new User();
		user.setEmail(requestMap.get("email"));
		user.setContactNumber(requestMap.get("contactNumber"));
		user.setName(requestMap.get("name"));
		user.setPassword(passwordEncoder.encode(requestMap.get("password")));
		user.setStatus("false");
		user.setRole("user");
		return user;
	}

	@Override
	public ResponseEntity<String> login(Map<String, String> requestMap) {
		log.info("inside login {}", requestMap);
		try {
			Authentication auth =
					authenticationManager.authenticate
					(new UsernamePasswordAuthenticationToken(requestMap.get("email"),
							requestMap.get("password")));
		if(auth.isAuthenticated()) {
			if(customerUsersDetailsService.getUserDetail().getStatus().equalsIgnoreCase("true")) {
				return new ResponseEntity<String>("{\"token\":\""+
						jwtService.generateToken(customerUsersDetailsService.getUserDetail().getEmail(),
								customerUsersDetailsService.getUserDetail().getRole()) + "\"}",
			HttpStatus.OK);
			}
			else {
				return ProductUtils.getResponseEntity(ProductConstants.WAIT_FOR_ADMIN_APPROVAL, HttpStatus.BAD_REQUEST);
			}
		}
		}
		catch(Exception ex) {
			System.out.println("***************EXCEPTION***********");
			ex.printStackTrace();
		}
		return ProductUtils.getResponseEntity(ProductConstants.BAD_CREDENTIALS, HttpStatus.BAD_REQUEST);
	}

	@Override
	public ResponseEntity<List<UserWrapper>> getAllUsers() {
		try {
			if(jwtAuthFilter.isAdmin()) {
				return new ResponseEntity<List<UserWrapper>>(userRepo.findAllUsers(),HttpStatus.OK);
			}else {
				return new ResponseEntity<>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<List<UserWrapper>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> update(Map<String, String> requestMap)  {
				try {
					if(jwtAuthFilter.isAdmin()) {
					 Optional<User> optional = userRepo.findById(Long.parseLong(requestMap.get("id")));
					 if(!optional.isEmpty()) {
						 userRepo.updateStatus(requestMap.get("status"),Long.parseLong(requestMap.get("id")));
						// sendMailToAllAdmin(requestMap.get("status"),optional.get().getEmail(),userRepo.findAllAdmins());
						 return new ResponseEntity<>("User status updated successfully" ,HttpStatus.OK);
					 }else {
						 return ProductUtils.getResponseEntity("User Id doesnt exist", HttpStatus.OK);
					 }
					}
					else {
						return ProductUtils.getResponseEntity(ProductConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
		return ProductUtils.getResponseEntity(ProductConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private void sendMailToAllAdmin(String status, String user, List<String> allAdmins) {
		allAdmins.remove(jwtAuthFilter.getCurrentUser());
		if(status!=null  &&  status.equalsIgnoreCase("true")) {
			emailUtils.sendSimpleMessage(jwtAuthFilter.getCurrentUser(), "Account Approved", "USER:- " + user+ "\n is approved by \nADMIN:- " +jwtAuthFilter.getCurrentUser() ,allAdmins);
		}else {
			emailUtils.sendSimpleMessage(jwtAuthFilter.getCurrentUser(), "Account Disabled"  , "USER:- " + user+ "\n is disabled by  \nADMIN:- " +jwtAuthFilter.getCurrentUser() ,allAdmins);
		}
	}

	@Override
	public ResponseEntity<String> checkToken() {
		return new ResponseEntity<>("true",HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> changePassword(Map<String,String> requestMap) {
		try { 
			User userObj = userRepo.findByEmail(jwtAuthFilter.getCurrentUser());
			if(!userObj.equals(null)) {
				if (!passwordEncoder.matches(requestMap.get("oldPassword"), userObj.getPassword())) {
					return ProductUtils.getResponseEntity(ProductConstants.INCORRECT_OLD_PASSWORD  ,HttpStatus.BAD_REQUEST);
		        }
		        // Encode and update the new password
		        String encodedPassword = passwordEncoder.encode(requestMap.get("newPassword"));
		        userObj.setPassword(encodedPassword);
		        userRepo.save(userObj);
		        return ResponseEntity.ok("Password changed successfully");
			}
			return ProductUtils.getResponseEntity(ProductConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ProductUtils.getResponseEntity(ProductConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}


	
}
  

