package com.bca.byc.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "status", columnDefinition = "boolean default true")
    private Boolean status = true;

    @Column(name = "is_commentable", columnDefinition = "boolean default true")
    private Boolean isCommentable = true;

    @Column(name = "is_shareable", columnDefinition = "boolean default true")
    private Boolean isShareable = true;

    @Column(name = "is_show_likes", columnDefinition = "boolean default true")
    private Boolean isShowLikes = true;

    @Column(name = "is_posted", columnDefinition = "boolean default false")
    private Boolean isPosted = false;

    @Column(name = "post_at")
    private LocalDateTime postAt;

    // relations

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user;

    @ManyToOne
    @JoinColumn(name = "post_category_id")
    private PostCategory postCategory;

    @ManyToMany
    @JoinTable(name = "post_has_tag",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags = new HashSet<>();

//    @ManyToMany
//    @JoinTable(name = "post_has_tag_users",
//            joinColumns = @JoinColumn(name = "post_id"),
//            inverseJoinColumns = @JoinColumn(name = "user_id"))
//    private Set<AppUser> tagUsers = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "post_location_id")
    private PostLocation postLocation;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LikeDislike> likeDislikes = new ArrayList<>();

}
