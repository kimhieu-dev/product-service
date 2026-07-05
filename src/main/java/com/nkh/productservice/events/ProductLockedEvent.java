package com.nkh.productservice.events;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductLockedEvent {
    private String orderId;
//    private String status;
}
