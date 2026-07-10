package com.nkh.productservice.service.impl;

import com.nkh.productservice.dto.request.CreateProductReq;
import com.nkh.productservice.dto.request.LockProductItem;
import com.nkh.productservice.dto.request.LockProductReq;
import com.nkh.productservice.dto.request.ProductFilter;
import com.nkh.productservice.entity.Product;
import com.nkh.productservice.events.ProductLockedEvent;
import com.nkh.productservice.exception.AppException;
import com.nkh.productservice.exception.ErrorCode;
import com.nkh.productservice.mapper.ProductMapper;
import com.nkh.productservice.repository.CategoryRepo;
import com.nkh.productservice.repository.ProductRepo;
import com.nkh.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.shaded.com.google.protobuf.RpcUtil;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementPermission;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepo productRepo;
    private final ProductMapper productMapper;
    private final CategoryRepo categoryRepo;
    private final KafkaTemplate<String,Object> kafkaTemplate;
    private final RedissonClient redissonClient;

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
        List<String> sortedIds = items.stream()
                .map(LockProductItem::getId)
                .sorted()
                .toList();
        String lockKey =  "lock:products:" + String.join(",",sortedIds);
        RLock lock = redissonClient.getLock(lockKey);
        try{
            if(lock.tryLock(10,5, TimeUnit.SECONDS)){
                Thread.sleep(4000);
                log.info("Acquired Redis lock for: {}",lockKey);
                Map<String,Integer> productIdQunatityMap = items.stream()
                .collect(Collectors.toMap(LockProductItem::getId, LockProductItem::getQuantity));

                List<Product> products = productRepo.findByIdIn(new ArrayList<>(productIdQunatityMap.keySet()));
                if (products.isEmpty()){
                    throw new RuntimeException("Product not found");
                }

                products.forEach(product -> {
                    int remainStock = product.getStock() - productIdQunatityMap.get(product.getId());
                    if (remainStock<0){
                        throw new RuntimeException("Product "+product.getId()+"is out of stock");
                    }
                    product.setStock(remainStock);
                });

                productRepo.saveAll(products);
            }else {
                throw new RuntimeException("Server busy. please try again later");
            }
        }catch (InterruptedException ex){
            Thread.currentThread().interrupt();
            throw new RuntimeException("Process interrupt");
        }finally {
            log.info("waiting for unlock [{}]",lockKey);
            if (lock.isHeldByCurrentThread()){
                lock.unlock();
                log.info("Unlock success for [{}]",lockKey);
            }
        }
    }

//    @Override
//    public void lock(LockProductReq request) {
//        List<LockProductItem> items = request.getItems();
//
//        Map<String,Integer> productIdQunatityMap = items.stream()
//                .collect(Collectors.toMap(LockProductItem::getId, LockProductItem::getQuantity));
//
//        List<Product> products = productRepo.findByIdIn(new ArrayList<>(productIdQunatityMap.keySet()));
//        // chua validate
//        if (products.isEmpty()){
//            throw new RuntimeException("Product not found");
//        }
//
//        products.forEach(product -> {
//            product.setStock(product.getStock() - productIdQunatityMap.get(product.getId()));
//        });
//
//        productRepo.saveAll(products);
//        log.info("product locked successfully , total {}",products.size());
//    }
}
