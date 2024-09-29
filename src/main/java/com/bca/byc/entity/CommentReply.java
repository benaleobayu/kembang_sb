package com.bca.byc.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "comment_reply", indexes = {
    @Index(name = "idx_comment_reply_secure_id", columnList = "secure_id", unique = true)
})
public class CommentReply extends AbstractBaseEntity implements SecureIdentifiable{

    @Override
    public String getSecureId() {
        return super.getSecureId();
    }

    @Override
    public Boolean getActive() {
        return super.isActive();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "comment", columnDefinition = "varchar(250)")
    private String comment;

    @Column(name = "status", columnDefinition = "boolean default true")
    private Boolean status = true;

    // relations
    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment parentComment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user;

    @OneToMany(mappedBy = "commentReply", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LikeDislike> likeDislikes = new ArrayList<>();

    // stats

    @Column(name = "likes_count")
    private Long LikesCount = 0L;

    @Column(name = "shares_count")
    private Long SharesCount = 0L;

    @Column(name = "comments_count")
    private Long CommentsCount = 0L;

}
