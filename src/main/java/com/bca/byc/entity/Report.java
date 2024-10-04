package com.bca.byc.entity;

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
public class Report extends AbstractBaseEntity implements SecureIdentifiable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @Column(name = "report")
    private String report;

    @Override
    public String getSecureId() {
        return super.getSecureId();
    }

    @Override
    public Boolean getActive() {
        return super.getIsActive();
    }
}
