package com.pratik.backendproductapp.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserWrapper {
	
	private Long id;
	
	private String email;
	
	private String name;
	
	private String contactNumber;
	
	private String role;
	
	private String status;

	public UserWrapper(Long id, String email, String name, String contactNumber, String status) {
		super();
		this.id = id;
		this.email = email;
		this.name = name;
		this.contactNumber = contactNumber;
		this.status = status;
	}
	
	

}
