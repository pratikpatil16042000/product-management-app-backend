package com.pratik.backendproductapp.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.pratik.backendproductapp.entity.Category;

public interface CategoryService {

	ResponseEntity<String> addNewCategory(Map<String, String> requestMap);

	ResponseEntity<List<Category>> getAllCategory(String filterValue);

	ResponseEntity<String> updateCategory(Map<String, String> requestMap);

}
