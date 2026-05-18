package com.nkh.productservice.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.sql.results.graph.collection.internal.BagInitializer;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateProductReq {
    @NotEmpty
    private String name;
    @NotNull
    private BigDecimal price;
    @NotNull
    private Integer stock;
    @NotEmpty
    private String categoryId;
}
