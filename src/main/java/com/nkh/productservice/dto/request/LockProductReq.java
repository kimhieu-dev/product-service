package com.nkh.productservice.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LockProductReq {
    @NotEmpty
    private List<LockProductItem> items;
}
