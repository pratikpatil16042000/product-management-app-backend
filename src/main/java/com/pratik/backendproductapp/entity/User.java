package com.pratik.backendproductapp.entity;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NamedQuery(name = "User.findByEmail",query = "select u from User u where u.email=:email")

@NamedQuery(name = "User.findAllUsers",query = "select new com.pratik.backendproductapp.wrapper.UserWrapper(u.id,u.email,u.status,u.contactNumber,u.name) from User u where u.role='user'")

@NamedQuery(name = "User.findAllAdmins",query = "select u.email from User u where u.role='admin'")

@NamedQuery(name = "User.updateStatus",query = "update User u set u.status=:status where u.id=:id")

@Entity
@Data
@Table(name="users")
@NoArgsConstructor
@AllArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String email;
	
	private String password;
	
	private String name;
	
	private String contactNumber;
	
	private String role;
	
	private String status;
	
}
