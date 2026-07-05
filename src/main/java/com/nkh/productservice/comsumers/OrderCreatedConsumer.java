package com.nkh.productservice.comsumers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nkh.productservice.events.OrderCreatedEvent;
import com.nkh.productservice.dto.request.LockProductItem;
import com.nkh.productservice.dto.request.LockProductReq;
import com.nkh.productservice.events.ProductLockedEvent;
import com.nkh.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCreatedConsumer {
    private final ProductService productService;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String,ProductLockedEvent> kafkaTemplate;


    @KafkaListener(topics = "order_created")
    @RetryableTopic(
            attempts = "4",
            backoff = @Backoff(delay = 2000, multiplier = 2.0),
            exclude = {NullPointerException.class, IllegalArgumentException.class}
    )
    public void handleOrderCreatedEvent(String orderString) throws JsonProcessingException {
//        ObjectMapper objectMapper = new ObjectMapper();
        OrderCreatedEvent orderCreatedEvent = objectMapper.readValue(orderString, OrderCreatedEvent.class);
        log.info("Receive order message: {}",orderCreatedEvent);

        List<LockProductItem> lockProductItems = new ArrayList<>();

        orderCreatedEvent.getOrderItems().forEach( orderItem -> {
            LockProductItem lockProductItem = new LockProductItem();
            lockProductItem.setId(orderItem.getProductId());
            lockProductItem.setQuantity(orderItem.getQuantity());
            lockProductItems.add(lockProductItem);
        });

        LockProductReq lockProductReq = new LockProductReq();
        lockProductReq.setItems(lockProductItems);
        productService.lock(lockProductReq);

        ProductLockedEvent productLockedEvent = new ProductLockedEvent();
        productLockedEvent.setOrderId(orderCreatedEvent.getId());
        kafkaTemplate.send("product_locked",productLockedEvent);
        log.info("Lock product success, items of: {}",orderCreatedEvent.getId());
    }

}
