package com.bca.byc.entity;

import com.bca.byc.entity.impl.SecureIdentifiable;
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
@Table(name = "post", indexes = {
    @Index(name = "idx_secure_id", columnList = "secure_id", unique = true)
})
public class Post extends AbstractBaseEntity implements SecureIdentifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Override
    public String getSecureId() {
        return super.getSecureId();
    }

    @Override
    public Boolean getActive() {
        return super.getIsActive();
    }

    @Column(name = "description", columnDefinition = "varchar(1000)")
    private String description;

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
    private BusinessCategory postCategory;

    @ManyToMany
    @JoinTable(name = "post_has_tag",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "post_location_id", nullable = true)
    private PostLocation postLocation;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LikeDislike> likeDislikes = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostContent> postContents = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserHasSavedPost> savedPosts = new ArrayList<>();


    // stats

    @Column(name = "likes_count")
    private Long LikesCount = 0L;

    @Column(name = "shares_count")
    private Long SharesCount = 0L;

    @Column(name = "comments_count")
    private Long CommentsCount = 0L;

}
