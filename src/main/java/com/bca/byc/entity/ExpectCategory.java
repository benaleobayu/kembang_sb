package com.bca.byc.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class ExpectCategory extends AbstractBaseEntity {

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "description", columnDefinition = "text")
    private String description;

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