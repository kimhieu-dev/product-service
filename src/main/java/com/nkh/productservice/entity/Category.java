package com.nkh.productservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "categories")
public class Category extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(generator = "uuid")
    @UuidGenerator
    private String id;

    private String name;

    @Column(name = "parent_id")
    private String parentId;
}
