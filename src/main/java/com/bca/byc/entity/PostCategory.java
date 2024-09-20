package com.bca.byc.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "post_category")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostCategory extends AbstractBaseEntityNoUUID {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "icon", columnDefinition = "text")
    private String icon;

    @Column(name = "name", columnDefinition = "varchar(255) default ''")
    private String name;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private AppAdmin createdBy;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    private AppAdmin updatedBy;

}
