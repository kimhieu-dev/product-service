package com.nkh.productservice.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LockProductItem {
    @NotEmpty
    private String id;

    @Positive
    @NotNull
    private Integer quantity;
}
