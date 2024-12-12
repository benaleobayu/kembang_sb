package com.kembang.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class ProductHasStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "stock_id")
    private ProductStock productStock;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "quantity")
    private Integer quantity;

}
