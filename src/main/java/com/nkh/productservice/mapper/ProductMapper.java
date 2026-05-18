package com.nkh.productservice.mapper;

import com.nkh.productservice.dto.request.CreateProductReq;
import com.nkh.productservice.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toProduct(CreateProductReq request);
}
