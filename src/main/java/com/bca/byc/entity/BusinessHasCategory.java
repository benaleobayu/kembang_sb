package com.bca.byc.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "business_has_category")
public class BusinessHasCategory extends AbstractBaseEntityNoUUID{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id")
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_category_parent_id")
    private BusinessCategory businessCategoryParent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_category_child_id")
    private BusinessCategory businessCategoryChild;

}

