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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pratik.backendproductapp.contants.ProductConstants;
import com.pratik.backendproductapp.entity.Bill;
import com.pratik.backendproductapp.service.BillService;
import com.pratik.backendproductapp.utils.ProductUtils;

@RestController
@RequestMapping(path = "/bill")
public class BillController {
	
	@Autowired
	BillService billService;
	
	@PostMapping(path = "/generateReport")
	public ResponseEntity<String> generateReport(@RequestBody Map<String,Object> requestMap ){
		try {
			return billService.generateReport(requestMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ProductUtils.getResponseEntity(ProductConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@GetMapping(path = "/getBills")
	public ResponseEntity<List<Bill>> getBills(){
		try {
			return billService.getBills();
		}catch(Exception e) {
			
		}
		return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PostMapping(path = "/getPdf")
	public ResponseEntity<byte[]> getPdf(@RequestBody Map<String,Object> requestMap ){
		try {
			return billService.getPdf(requestMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(new byte[1], HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@DeleteMapping(path = "/delete/{id}")
	public ResponseEntity<String> deleteBill(@PathVariable Integer id ){
		try {
			return billService.deleteBill(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ProductUtils.getResponseEntity(ProductConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	

}
