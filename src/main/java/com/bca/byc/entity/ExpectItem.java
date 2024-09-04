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
public class ExpectItem extends AbstractBaseEntityNoUUID {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @ManyToOne
    @JoinColumn(name = "expect_category_id")
    private ExpectCategory expectCategory;

    @OneToMany(mappedBy = "expectItem")
    private List<UserHasExpect> userHasExpects = new ArrayList<>();
}