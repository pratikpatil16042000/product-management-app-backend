package com.pratik.backendproductapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.pratik.backendproductapp.contants.ProductConstants;
import com.pratik.backendproductapp.entity.Category;
import com.pratik.backendproductapp.entity.Product;
import com.pratik.backendproductapp.jwt.filter.JwtAuthFilter;
import com.pratik.backendproductapp.repository.ProductRepository;
import com.pratik.backendproductapp.utils.ProductUtils;
import com.pratik.backendproductapp.wrapper.ProductWrapper;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class ProductServiceImpl implements ProductService{
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	JwtAuthFilter jwtAuthFilter;

	@Override
	public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {
		log.info("Inside addNewProduct()");
		try {
			if(jwtAuthFilter.isAdmin()) {
				if(validateProduct(requestMap,false)) {
					productRepository.save(getProductFromMap(requestMap, false));
					return ProductUtils.getResponseEntity(ProductConstants.PRODUCT_ADDED_SUCCESSFULLY, HttpStatus.OK);
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

	private boolean validateProduct(Map<String, String> requestMap, boolean validateId) {
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
	
	private Product getProductFromMap(Map<String,String>requestMap, boolean isAdd) {
		Category category = new Category();
		category.setId(Integer.parseInt(requestMap.get("categoryId")));
		Product product = new Product();
		if(isAdd) {
			product.setId(Long.parseLong(requestMap.get("id")));
		}else {
			product.setStatus("true");
		}
		product.setCategory(category);
		product.setName(requestMap.get("name"));
		product.setDescription(requestMap.get("description"));
		product.setPrice(Integer.parseInt(requestMap.get("price")));
		return product;
	}

	@Override
	public ResponseEntity<List<ProductWrapper>> getAllProducts() {
		try {
				return new ResponseEntity<List<ProductWrapper>>(productRepository.findAllProducts(),HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
		try {
			if(jwtAuthFilter.isAdmin()) {
				if(validateProduct(requestMap, true)) {
					Optional<Product> optional = productRepository.findById(Long.parseLong(requestMap.get("id")));
					if(!optional.isEmpty()) {
						Product product = getProductFromMap(requestMap, true);
						product.setStatus(optional.get().getStatus());
						productRepository.save(product);
						return ProductUtils.getResponseEntity(ProductConstants.PRODUCT_UPDATED_SUCCESSFULLY, HttpStatus.OK);
					}else {
						return ProductUtils.getResponseEntity(ProductConstants.PRODUCT_DOES_NOT_EXIST, HttpStatus.OK);
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

	@Override
	public ResponseEntity<String> deleteProduct(Long id) {
		try {
			if(jwtAuthFilter.isAdmin()) {
				Optional<Product> optional = productRepository.findById(id);
				if(!optional.isEmpty()) {
					productRepository.deleteById(id);
					return ProductUtils.getResponseEntity(ProductConstants.PRODUCT_DELETED_SUCCESSFULLY, HttpStatus.OK);
				}
				return ProductUtils.getResponseEntity(ProductConstants.PRODUCT_DOES_NOT_EXIST,HttpStatus.OK);
				}
			else {
				return ProductUtils.getResponseEntity(ProductConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ProductUtils.getResponseEntity(ProductConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
		
	}

	@Override
	public ResponseEntity<String> updateStatus(Map<String, String> requestMap) {
		try {
			if(jwtAuthFilter.isAdmin()) {
				Optional<Product> optional = productRepository.findById(Long.parseLong(requestMap.get("id")));
				if(!optional.isEmpty()) {
					productRepository.updateProductStatus(requestMap.get("status"),Long.parseLong(requestMap.get("id")));
					return ProductUtils.getResponseEntity(ProductConstants.PRODUCT_STATUS_UPDATED_SUCCESSFULLY, HttpStatus.OK);
				}
				return ProductUtils.getResponseEntity(ProductConstants.PRODUCT_DOES_NOT_EXIST,HttpStatus.OK);
				}
			else {
				return ProductUtils.getResponseEntity(ProductConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ProductUtils.getResponseEntity(ProductConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<List<ProductWrapper>> getProductByCategory(Integer id) {
		try {
			return new ResponseEntity<>(productRepository.findProductByCategory(id),HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<ProductWrapper> getProductById(Long id) {
		try {
			return new ResponseEntity<>(productRepository.findProductById(id),HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(new ProductWrapper(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
	


