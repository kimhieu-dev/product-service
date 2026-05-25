package com.nkh.productservice.service.impl;

import com.nkh.productservice.dto.request.CreateProductReq;
import com.nkh.productservice.dto.request.LockProductItem;
import com.nkh.productservice.dto.request.LockProductReq;
import com.nkh.productservice.dto.request.ProductFilter;
import com.nkh.productservice.entity.Product;
import com.nkh.productservice.exception.AppException;
import com.nkh.productservice.exception.ErrorCode;
import com.nkh.productservice.mapper.ProductMapper;
import com.nkh.productservice.repository.CategoryRepo;
import com.nkh.productservice.repository.ProductRepo;
import com.nkh.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementPermission;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        }
        Product product = productMapper.toProduct(request);
        return productRepo.save(product);
    }

    @Override
    public List<Product> search(ProductFilter productFilter) {
        return productRepo.findByIdIn(productFilter.getProductIds());
    }

    @Override
    public void lock(LockProductReq request) {
        List<LockProductItem> items = request.getItems();

        Map<String,Integer> productIdQunatityMap = items.stream()
                .collect(Collectors.toMap(LockProductItem::getId, LockProductItem::getQuantity));

        List<Product> products = productRepo.findByIdIn(new ArrayList<>(productIdQunatityMap.keySet()));
        // chua validate

        products.forEach(product -> {
            product.setStock(product.getStock() - productIdQunatityMap.get(product.getId()));
        });

        productRepo.saveAll(products);
    }
}
