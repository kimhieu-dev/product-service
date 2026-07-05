package com.nkh.productservice.events;

import com.nkh.productservice.dto.Order;
import com.nkh.productservice.dto.OrderItem;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderCreatedEvent extends Order {
    private List<OrderItem> orderItems;
}
