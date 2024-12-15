package com.kembang.entity;

import com.kembang.entity.impl.SecureIdentifiable;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "order_products", indexes = {@Index(name = "order_products_secure_id", columnList = "secure_id", unique = true)})
public class Order extends AbstractBaseEntity implements SecureIdentifiable {

    @Override
    public Long getId() {
        return super.getId();
    }

    @Override
    public String getSecureId() {
        return super.getSecureId();
    }

    @Override
    public Boolean getIsActive() {
        return super.getIsActive();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private AppUser customer;

    @Column(name = "forward_name")
    private String forwardName;

    @Column(name = "forward_address")
    private String forwardAddress;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderHasProduct> orderProducts;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "short_note", columnDefinition = "varchar(100)")
    private String shortNote;

    @Column(name = "delivery_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate deliveryDate;

    @Column(name = "orders")
    private Integer orders;

    @Column(name = "is_paid")
    private Boolean isPaid;

    @ManyToOne
    @JoinColumn(name = "route_id", referencedColumnName = "secure_id")
    private OrderRoute orderRoute;

    @Column(name = "cost_route")
    private Integer costRoute;

    @Column(name = "cost_order")
    private Integer costOrder;
}
