package com.nkh.productservice.service.impl;

import com.nkh.productservice.dto.request.CreateProductReq;
import com.nkh.productservice.entity.Product;
import com.nkh.productservice.exception.AppException;
import com.nkh.productservice.mapper.ProductMapper;
import com.nkh.productservice.repository.CategoryRepo;
import com.nkh.productservice.repository.ProductRepo;
import com.nkh.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepo productRepo;
    private final ProductMapper productMapper;
    private final CategoryRepo categoryRepo;

    @Override
    public Product create(CreateProductReq request) {
        var existedCategoryOptional = categoryRepo.findById(request.getCategoryId());
        if (existedCategoryOptional.isEmpty()) {
            throw new AppException("Category Not Found");
        }
        Product product = productMapper.toProduct(request);
        return productRepo.save(product);
    }
}
