package com.pratik.backendproductapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.pratik.backendproductapp.contants.ProductConstants;
import com.pratik.backendproductapp.entity.Category;
import com.pratik.backendproductapp.jwt.filter.JwtAuthFilter;
import com.pratik.backendproductapp.repository.CategoryRepository;
import com.pratik.backendproductapp.utils.ProductUtils;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService{
	
	@Autowired
	CategoryRepository categoryRepository;
	
	@Autowired
	JwtAuthFilter jwtAuthFilter;

	@Override
	public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
		try {
			if(jwtAuthFilter.isAdmin()) {
				if(validateCategory(requestMap,false)) {
					categoryRepository.save(getCategoryFromMap(requestMap, false));
					return ProductUtils.getResponseEntity(ProductConstants.CATEGORY_ADDED_SUCCESSFULLY, HttpStatus.OK);
				}
				return ProductUtils.getResponseEntity(ProductConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
			}
			else {
				return ProductUtils.getResponseEntity(ProductConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ProductUtils.getResponseEntity(ProductConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private boolean validateCategory(Map<String, String> requestMap, boolean validateId) {
		if(requestMap.containsKey("name")) {
			if(requestMap.containsKey("id") && validateId) {
				return true;
			}
			else if (! validateId){
				return true;
			}
		}
		return false;
	}
	
	private Category getCategoryFromMap(Map<String,String>requestMap, boolean isAdd) {
		Category category = new Category();
		if(isAdd) {
			category.setId(Integer.parseInt(requestMap.get("id")));
		}
		category.setName(requestMap.get("name"));
		return category;
	}

	@Override
	public ResponseEntity<List<Category>> getAllCategory(String filterValue) {
		try {
			if(!Strings.isNullOrEmpty(filterValue) && filterValue.equalsIgnoreCase("true")) {
				log.info("Inside if ");
				return new ResponseEntity<List<Category>>(categoryRepository.findAllCategory(),HttpStatus.OK);
			}
			return new ResponseEntity<List<Category>>(categoryRepository.findAll(),HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
		try {
			if(jwtAuthFilter.isAdmin()) {
				if(validateCategory(requestMap, true)) {
					Optional<Category> optional = categoryRepository.findById(Integer.parseInt(requestMap.get("id")));
					if(!optional.isEmpty()) {
						categoryRepository.save(getCategoryFromMap(requestMap, true));
						return ProductUtils.getResponseEntity(ProductConstants.CATEGORY_UPDATED_SUCCESSFULLY, HttpStatus.OK);
					}else {
						return ProductUtils.getResponseEntity(ProductConstants.CATEGORY_DOES_NOT_EXIST, HttpStatus.OK);
					}
				}
				return ProductUtils.getResponseEntity(ProductConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
			}else {
				return ProductUtils.getResponseEntity(ProductConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ProductUtils.getResponseEntity(ProductConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
	


