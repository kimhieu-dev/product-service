package com.nkh.productservice.service;

import com.nkh.productservice.dto.request.CreateProductReq;
import com.nkh.productservice.dto.request.LockProductReq;
import com.nkh.productservice.dto.request.ProductFilter;
import com.nkh.productservice.entity.Product;

import java.util.List;

public interface ProductService {
    Product create(CreateProductReq request);
    List<Product> search(ProductFilter productFilter);
    void lock(LockProductReq request);
}
