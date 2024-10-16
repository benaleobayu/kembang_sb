package com.bca.byc.entity;

import com.bca.byc.entity.impl.SecureIdentifiable;
import com.bca.byc.validator.annotation.ReportStatusEnum;
import com.bca.byc.validator.annotation.ReportTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "report", indexes = {
        @Index(name = "idx_post_report", columnList = "post_id, user_id")
})
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Report extends AbstractBaseEntityCms implements SecureIdentifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ReportStatusEnum
    @Column(name = "status")
    private String status ;

    @ReportTypeEnum
    @Column(name = "type")
    private String type;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "reply_id")
    private CommentReply commentReply;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser reportedUser;

    @ManyToOne
    @JoinColumn(name = "reporter_id")
    private AppUser reporterUser;

    @Column(name = "reason", columnDefinition = "text")
    private String reason;

    @Column(name = "other_reason", columnDefinition = "text")
    private String otherReason;

    @Override
    public String getSecureId() {
        return super.getSecureId();
    }

    @Override
    public Boolean getIsActive() {
        return super.getIsActive();
    }
}
