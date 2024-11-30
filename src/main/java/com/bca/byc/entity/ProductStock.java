package com.bca.byc.entity;

import com.bca.byc.entity.impl.SecureIdentifiable;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "product_stocks", indexes = {@Index(name = "uk_product_stocks_secure_id", columnList = "secure_id", unique = true)})
public class ProductStock extends AbstractBaseEntity implements SecureIdentifiable {

    @Override public Long getId() {return super.getId();}

    @Override public String getSecureId() {return super.getSecureId();}

    @Override public Boolean getIsActive() {return super.getIsActive();}

    private LocalDate stockDate;

    @OneToMany(mappedBy = "productStock")
    private List<ProductHasStock> productHasStock;

}
