package com.nkh.productservice.repository;

import com.nkh.productservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product,String> {
    List<Product> findByIdIn(List<String> ids);
}
