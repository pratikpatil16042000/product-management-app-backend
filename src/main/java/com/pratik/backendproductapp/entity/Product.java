package com.pratik.backendproductapp.entity;


import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NamedQuery(name = "Product.findAllProducts",query = "select new com.pratik.backendproductapp.wrapper.ProductWrapper(p.id,p.name,p.description,p.price,p.status,p.category.id,p.category.name) from Product p")

@NamedQuery(name = "Product.updateProductStatus",query = "update Product p set p.status=:status where p.id=:id")

@NamedQuery(name = "Product.findProductByCategory",query = "select new com.pratik.backendproductapp.wrapper.ProductWrapper(p.id,p.name) from Product p where p.category.id=:id and p.status='true'")

@NamedQuery(name = "Product.findProductById",query = "select new com.pratik.backendproductapp.wrapper.ProductWrapper(p.id,p.name,p.description,p.price) from Product p where p.id=:id")


@Entity
@Data
@Table(name="products")
@NoArgsConstructor
@AllArgsConstructor
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_fk",nullable = false)
	private Category category;
	
	private String description;
	
	private Integer price;
	
	private String status;
	
	
	
	
	

	
}
