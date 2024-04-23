package com.pratik.backendproductapp.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductWrapper {
	
	Long id;
	
	String name;
	
	String description;
	
	Integer price;
	
	String status;
	
	Integer categoryId;
	
	String categoryName;

	public ProductWrapper(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public ProductWrapper(Long id, String name, String description, Integer price) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
	}
	
	
	
	
	
	

}
