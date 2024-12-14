package com.kembang.entity;

import com.kembang.entity.impl.SecureIdentifiable;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(
        name = "order_route",
        indexes = {@Index(name = "order_products_secure_id", columnList = "secure_id", unique = true)},
        uniqueConstraints = {@UniqueConstraint(columnNames = {"route", "date"})}
)

public class OrderRoute extends AbstractBaseEntity implements SecureIdentifiable {

    @Override public Long getId() {return super.getId();}

    @Override public String getSecureId() {return super.getSecureId();}

    @Override public Boolean getIsActive() {return super.getIsActive();}

    @Column(name = "route")
    private Integer route;

    @Column(name = "driver_name")
    private String driverName;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "total_distance")
    private BigDecimal totalDistance;

    @Column(name = "total_cost")
    private Integer totalCost;

    @Column(name = "total_remaining_cost")
    private Integer totalRemainingCost;

    @OneToMany(mappedBy = "orderRoute")
    private List<Order> orders;
}
