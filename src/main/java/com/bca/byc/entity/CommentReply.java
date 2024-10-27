package com.bca.byc.entity;

import com.bca.byc.entity.impl.SecureIdentifiable;
import com.bca.byc.validator.annotation.ReportStatusEnum;
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
public class CommentReply extends AbstractBaseEntity implements SecureIdentifiable {

    @Override
    public String getSecureId() {
        return super.getSecureId();
    }

    @Override
    public Boolean getIsActive() {
        return super.getIsActive();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "comment", columnDefinition = "varchar(250)")
    private String comment;

    @Column(name = "status")
    private Boolean status = true;

    @ReportStatusEnum
    @Column(name = "report_status")
    private String reportStatus = "REQUEST";

    // relations
    @ManyToOne
    @JoinColumn(name = "comment_id")
    @EqualsAndHashCode.Exclude
    private Comment parentComment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Exclude
    private AppUser user;

    @OneToMany(mappedBy = "commentReply", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> reports = new ArrayList<>();

    // stats

    @Column(name = "likes_count")
    private Long LikesCount = 0L;

    @Column(name = "comments_count")
    private Long CommentsCount = 0L;

}
