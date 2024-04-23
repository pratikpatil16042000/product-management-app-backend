package com.pratik.backendproductapp.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.pratik.backendproductapp.wrapper.ProductWrapper;

public interface ProductService {

	ResponseEntity<String> addNewProduct(Map<String, String> requestMap);

	ResponseEntity<List<ProductWrapper>> getAllProducts();

	ResponseEntity<String> updateProduct(Map<String, String> requestMap);

	ResponseEntity<String> deleteProduct(Long id);

	ResponseEntity<String> updateStatus(Map<String, String> requestMap);

	ResponseEntity<List<ProductWrapper>> getProductByCategory(Integer id);

	ResponseEntity<ProductWrapper> getProductById(Long id);

}
