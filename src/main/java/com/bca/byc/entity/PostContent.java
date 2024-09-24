package com.bca.byc.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "post_content")
public class PostContent extends AbstractBaseEntityNoUUID {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "index")
    private Integer index;

    @Column(name = "original_name", columnDefinition = "text")
    private String originalName;

    @Column(name = "thumbnail", columnDefinition = "text")
    private String thumbnail;

    @Column(name = "content", columnDefinition = "text")
    private String content;

    @Column(name = "type")
    private String type;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToMany
    @JoinTable(name = "post_content_has_tag_users",
            joinColumns = @JoinColumn(name = "post_content_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<AppUser> tagUsers = new HashSet<>();

}
