package com.kembang.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "order_has_product", indexes = {@Index(name = "uk_order_has_product_secure_id", columnList = "secure_id", unique = true)})
public class OrderHasProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "secure_id", nullable = false, unique = true, columnDefinition = "char(36) default gen_random_uuid()")
    private String secureId = UUID.randomUUID().toString();

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "description", columnDefinition = "text")
    private String orderNote;

    @Column(name = "quantity")
    private Integer quantity = 1;

    @Column(name = "total_price")
    private Integer totalPrice;
}
