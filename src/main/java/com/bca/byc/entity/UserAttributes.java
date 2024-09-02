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

    @Column(name = "is_recommended", columnDefinition = "boolean default false")
    private Boolean isRecommended;
    @Column(name = "is_verified", columnDefinition = "boolean default false")
    private Boolean isVerified;
    @Column(name = "is_blocked", columnDefinition = "boolean default false")
    private Boolean isBlocked;

    @OneToOne
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Exclude
    private User user;

}
