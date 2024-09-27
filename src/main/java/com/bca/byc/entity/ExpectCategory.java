package com.bca.byc.entity;

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
@Table(name = "expect_category", indexes = {
    @Index(name = "expect_category_secure_id", columnList = "secure_id")
})
public class ExpectCategory extends AbstractBaseEntity implements SecureIdentifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Override
    public String getSecureId() {
        return super.getSecureId();
    }

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "is_other", columnDefinition = "boolean default false")
    private Boolean isOther;

    @Column(name = "orders", columnDefinition = "int default 1")
    private Integer orders;

    @Column(name = "status", columnDefinition = "boolean default true")
    private Boolean status;

    // relations
    @OneToMany(mappedBy = "expectCategory")
    private List<ExpectItem> expectItems = new ArrayList<>();

    @OneToMany(mappedBy = "expectCategory")
    private List<UserHasExpect> userHasExpects = new ArrayList<>();

}