package com.praveen.Demo_Products.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import com.praveen.Demo_Products.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
	
@Query(value = "SELECT * FROM prod_tbl WHERE product_id = :id", nativeQuery=true) //sql database column name and table name tobe used
Optional<Product> findProductById(@RequestParam("id") String id);

}
