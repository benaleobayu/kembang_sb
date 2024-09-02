package com.bca.byc.entity;

import com.bca.byc.validation.ApproveEnum;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "user_attributes")
@Data
public class UserAttributes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApproveEnum
    @Column(name = "approved_by", columnDefinition = "varchar(20) default 'SYSTEM'")
    private String approvedBy;

    private Boolean isRecommended;
    private Boolean isVerified;
    private Boolean isBlocked;

    @OneToOne
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Exclude
    private User user;

}
