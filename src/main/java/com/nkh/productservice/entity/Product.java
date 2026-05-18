package com.nkh.productservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(generator = "uuid")
    @UuidGenerator
    private String id;

    private String name;

    private BigDecimal price;

    private Integer stock;

    @Column(name = "category_id")
    private String categoryId;
}
