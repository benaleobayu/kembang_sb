package com.bca.byc.entity;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "post")
public class Post extends AbstractBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "name")
    private String name;

    @Column(name = "content", columnDefinition = "text")
    private String content;

    @Column(name = "type")
    private String type;

    @Column(name = "status", columnDefinition = "boolean default true")
    private Boolean status = true;
}
