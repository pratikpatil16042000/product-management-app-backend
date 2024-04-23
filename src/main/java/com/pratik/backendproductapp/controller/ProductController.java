package com.pratik.backendproductapp.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pratik.backendproductapp.contants.ProductConstants;
import com.pratik.backendproductapp.service.ProductService;
import com.pratik.backendproductapp.utils.ProductUtils;
import com.pratik.backendproductapp.wrapper.ProductWrapper;

@RestController
@RequestMapping(path = "/products")
public class ProductController {
	
	@Autowired
	ProductService productService;
	
	@PostMapping(path = "/add")
	public ResponseEntity<String> addNewProduct(@RequestBody(required = true) Map<String,String> requestMap ){
		try {
			return productService.addNewProduct(requestMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ProductUtils.getResponseEntity(ProductConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@GetMapping(path = "/get")
	public ResponseEntity<List<ProductWrapper>> getAllProducts ( ){
		try {
			return productService.getAllProducts();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PutMapping(path = "/update")
	public ResponseEntity<String> updateProduct(@RequestBody(required = true) Map<String,String> requestMap ){
		try {
			return productService.updateProduct(requestMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ProductUtils.getResponseEntity(ProductConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@DeleteMapping(path = "/delete/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable Long id){
		try {
			return productService.deleteProduct(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ProductUtils.getResponseEntity(ProductConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PutMapping(path = "/updateStatus")
	public ResponseEntity<String> updateStatus(@RequestBody Map<String,String> requestMap){
		try {
			return productService.updateStatus(requestMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ProductUtils.getResponseEntity(ProductConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@GetMapping(path = "/getByCategory/{id}")
	public ResponseEntity<List<ProductWrapper>> getProductByCategory(@PathVariable Integer id){
		try {
			return productService.getProductByCategory(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@GetMapping(path = "/getById/{id}")
	public ResponseEntity<ProductWrapper> getProductById(@PathVariable Long id){
		try {
			return productService.getProductById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(new ProductWrapper(),HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	

}
