package com.bca.byc.entity;

import com.bca.byc.entity.impl.AttrIdentificable;
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
@Table(name = "comments" ,indexes = {
    @Index(name = "idx_comments_secure_id", columnList = "secure_id", unique = true)
})
public class Comment extends AbstractBaseEntity implements SecureIdentifiable {

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
    private String reportStatus ;

    // relations
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentReply> commentReply = new ArrayList<>();

    // stats

    @Column(name = "likes_count")
    private Long LikesCount = 0L;

    @Column(name = "comments_count")
    private Long CommentsCount = 0L;

}
