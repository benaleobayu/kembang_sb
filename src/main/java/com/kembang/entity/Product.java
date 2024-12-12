package com.kembang.entity;

import com.kembang.entity.impl.SecureIdentifiable;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "products", indexes = {@Index(name = "uk_products_secure_id", columnList = "secure_id", unique = true)})
public class Product extends AbstractBaseEntity implements SecureIdentifiable {

    @Override public Long getId() {return super.getId();}

    @Override public String getSecureId() {return super.getSecureId();}

    @Override public Boolean getIsActive() {return super.getIsActive();}

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "code", unique = true)
    private String code;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "price")
    private Integer price;

    @Column(name = "image", columnDefinition = "text")
    private String image;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private ProductCategory category;

}
