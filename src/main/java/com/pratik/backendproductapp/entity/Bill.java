package com.pratik.backendproductapp.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NamedQuery(name = "Bill.findAllBills",query = "select b from Bill b order by b.id desc")

@NamedQuery(name = "Bill.findBillByUserName",query = "select b from Bill b where b.createdBy=:userName order by b.id desc")

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bill {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String uuid;
	
	private String name;
	
	private String email;
	
	private String contactNumber;
	
	private String paymentMethod;
	
	private Integer totalAmount;
	
	@Column(name = "productDetail",columnDefinition = "json")
	private String productDetail;
	
	private String createdBy;
	
	
	
	
	

	
}
