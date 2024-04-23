package com.pratik.backendproductapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pratik.backendproductapp.entity.Bill;

@Repository
public interface BillRepository extends JpaRepository<Bill, Integer>{

	List<Bill> findAllBills();

	List<Bill> findBillByUserName(@Param("userName") String userName);

}
