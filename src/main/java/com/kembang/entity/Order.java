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

    @Column(name = "order_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate orderDate;

    @Column(name = "delivery_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate deliveryDate;

    @Column(name = "driver_name")
    private String driverName;

    @Column(name = "route")
    private Integer route;

    @Column(name = "is_paid")
    private Boolean isPaid;
}
