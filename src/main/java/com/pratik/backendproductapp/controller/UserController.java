package com.pratik.backendproductapp.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pratik.backendproductapp.contants.ProductConstants;
import com.pratik.backendproductapp.service.UserService;
import com.pratik.backendproductapp.utils.ProductUtils;
import com.pratik.backendproductapp.wrapper.UserWrapper;

@RestController
@RequestMapping(path = "/users")
public class UserController {

	@Autowired
	private UserService userService;
	
	@PostMapping(path = "/signup")
	public ResponseEntity<String> signUp(@RequestBody(required = true) Map<String , String> requestMap){
		try {
			return userService.signUp(requestMap);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return ProductUtils.getResponseEntity(ProductConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PostMapping(path = "/login")
	public ResponseEntity<String> login(@RequestBody(required = true) Map<String , String> requestMap){
		try {
			System.out.println("inside try of Controller");
			return userService.login(requestMap);
		}
		catch (Exception ex) {
			System.out.println("inside catch of Controller");
			ex.printStackTrace();
		}
		System.out.println("Login() failed");
		return ProductUtils.getResponseEntity(ProductConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@GetMapping(path = "/get")
	public ResponseEntity<List<UserWrapper>> getAllUsers(){
		try {
			return userService.getAllUsers();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ResponseEntity<List<UserWrapper>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PutMapping(path = "/update")
	public ResponseEntity<String> update(@RequestBody(required = true) Map<String , String> requestMap){
		try {
			return userService.update(requestMap);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return ProductUtils.getResponseEntity(ProductConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@GetMapping(path = "/checkToken")
	public ResponseEntity<String> checkToken(){
		try {
			return userService.checkToken();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ProductUtils.getResponseEntity(ProductConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PostMapping(path = "/changePassword")
	public ResponseEntity<String> changePassword(@RequestBody Map<String,String> requestMap){
		try {
			return userService.changePassword(requestMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ProductUtils.getResponseEntity(ProductConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}

