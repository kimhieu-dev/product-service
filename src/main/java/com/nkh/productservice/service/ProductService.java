package com.nkh.productservice.service;

import com.nkh.productservice.dto.request.CreateProductReq;
import com.nkh.productservice.entity.Product;

public interface ProductService {
    Product create(CreateProductReq request);
}
