package com.bca.byc.entity;

import com.bca.byc.entity.impl.SecureIdentifiable;
import com.bca.byc.entity.impl.AttrIdentificable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "expect_item", indexes = {
    @Index(name = "expect_item_secure_id", columnList = "secure_id", unique = true)
})
public class ExpectItem extends AbstractBaseEntityCms implements SecureIdentifiable, AttrIdentificable {

    @Override
    public String getSecureId() {
        return super.getSecureId();
    }

    @Override
    public Boolean getActive() {
        return super.getIsActive();
    }

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "is_other", columnDefinition = "boolean default false")
    private Boolean isOther;

    @Column(name = "orders", columnDefinition = "int default 1")
    private Integer orders;

    // relations

    @ManyToOne
    @JoinColumn(name = "expect_category_id")
    private ExpectCategory expectCategory;

    @OneToMany(mappedBy = "expectItem")
    private List<UserHasExpect> userHasExpects = new ArrayList<>();

    @Override
    public String getName() {
        return name;
    }
}