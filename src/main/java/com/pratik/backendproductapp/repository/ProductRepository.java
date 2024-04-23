package com.pratik.backendproductapp.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pratik.backendproductapp.entity.Product;
import com.pratik.backendproductapp.wrapper.ProductWrapper;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

	List<ProductWrapper> findAllProducts();

	@Transactional
	@Modifying
	Integer updateProductStatus(@Param("status") String status, @Param("id") Long id);

	List<ProductWrapper> findProductByCategory(@Param("id") Integer id);

	ProductWrapper findProductById(Long id);
	

}
