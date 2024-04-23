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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pratik.backendproductapp.contants.ProductConstants;
import com.pratik.backendproductapp.entity.Category;
import com.pratik.backendproductapp.service.CategoryService;
import com.pratik.backendproductapp.utils.ProductUtils;

@RestController
@RequestMapping(path = "/category")
public class CategoryController {
	
	@Autowired
	CategoryService categoryService;
	
	@PostMapping(path = "/add")
	public ResponseEntity<String> addNewCategory(@RequestBody(required = true) Map<String,String> requestMap ){
		try {
			return categoryService.addNewCategory(requestMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ProductUtils.getResponseEntity(ProductConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@GetMapping(path = "/get")
	public ResponseEntity<List<Category>> getAllCategory(@RequestParam(required = false) String filterValue ){
		try {
			return categoryService.getAllCategory(filterValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PutMapping(path = "/update")
	public ResponseEntity<String> updateCategory(@RequestBody(required = true) Map<String,String> requestMap ){
		try {
			return categoryService.updateCategory(requestMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ProductUtils.getResponseEntity(ProductConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
