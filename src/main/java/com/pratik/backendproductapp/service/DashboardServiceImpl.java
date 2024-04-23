package com.pratik.backendproductapp.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.pratik.backendproductapp.repository.BillRepository;
import com.pratik.backendproductapp.repository.CategoryRepository;
import com.pratik.backendproductapp.repository.ProductRepository;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class DashboardServiceImpl implements DashboardService{
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	CategoryRepository categoryRepository;
	
	@Autowired
	BillRepository billRepository;

	
	@Override
	public ResponseEntity<Map<String, Object>> getCount() {
		log.info("Inside getCount()");
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("category",categoryRepository.count());
		map.put("product",productRepository.count());
		map.put("bill",billRepository.count());
		return new ResponseEntity<>(map,HttpStatus.OK);
		
	}
	
	
	

}
	


