package com.nkh.productservice.controller;

import com.nkh.productservice.dto.BaseResponse;
import com.nkh.productservice.dto.request.CreateProductReq;
import com.nkh.productservice.dto.request.ProductFilter;
import com.nkh.productservice.entity.Product;
import com.nkh.productservice.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
@Slf4j
@Validated
public class ProductController {
    private final ProductService productService;

    @PostMapping()
    public BaseResponse<Product> create(@RequestBody @Valid CreateProductReq request){
        return BaseResponse.success(productService.create(request));
    }

    @PostMapping("/search")
    public BaseResponse<List<Product>> search(@RequestBody ProductFilter productFilter){
        List<Product> products = productService.search(productFilter);
        return BaseResponse.success(products);
    }

}
