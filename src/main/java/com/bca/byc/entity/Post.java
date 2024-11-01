package com.bca.byc.entity;

import com.bca.byc.entity.impl.SecureIdentifiable;
import com.bca.byc.validator.annotation.ReportStatusEnum;
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
public class Post extends AbstractBaseEntityCms implements SecureIdentifiable {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;

    @Version
    @Column(name = "version")
    private Long version;

    @Override
    public String getSecureId() {
        return super.getSecureId();
    }

    @Override
    public Boolean getIsActive() {
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

    @Column(name = "content_type")
    private String contentType = "IMAGE";

    @ReportStatusEnum
    @Column(name = "report_status")
    private String reportStatus = "NULL";

    // relations on post user

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

    @OneToMany(mappedBy = "post", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LikeDislike> likeDislikes = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostContent> postContents = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserHasSavedPost> savedPosts = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> reports = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostHasTags> postHasTags = new ArrayList<>();

    // relation on admin post

    @ManyToOne
    @JoinColumn(name = "channel_id")
    private Channel channel;

    @Column(name = "highlight", columnDefinition = "text")
    private String highlight;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private AppAdmin admin;

    @Column(name = "is_admin_post")
    private Boolean isAdminPost = false;

    @Column(name = "is_teaser")
    private Boolean isTeaser = false;

    // promoted

    @Column(name = "promoted_status")
    private String promotedStatus = "NOT_DEFINED";

    @Column(name = "promoted_active")
    private Boolean promotedActive = false;

    @Column(name = "promoted_at")
    private LocalDateTime promotedAt;

    @Column(name = "promoted_until")
    private LocalDateTime promotedUntil;

    @Embedded
    private PostStats stats = new PostStats();

    public void setLikesCount(Long likesCount) {
        if (likesCount < 0) {
            throw new IllegalArgumentException("Likes count cannot be negative");
        }
        this.stats.setLikesCount(likesCount);
    }
}

