package com.pratik.backendproductapp.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pratik.backendproductapp.service.DashboardService;

@RestController
@RequestMapping(path = "/dashboard")
public class DashboardController {
	
	@Autowired
	DashboardService dashboardService;
	
	@GetMapping(path = "/details")
	ResponseEntity<Map<String,Object>> getCount(){
		return dashboardService.getCount();
	}
	
	
	

}
