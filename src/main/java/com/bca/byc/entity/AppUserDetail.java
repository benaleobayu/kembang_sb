package com.bca.byc.entity;

import com.bca.byc.entity.impl.SecureIdentifiable;
import com.bca.byc.enums.StatusType;
import com.bca.byc.enums.UserType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;


@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "app_user_detail", indexes = {
    @Index(name = "aud_secure_id", columnList = "secure_id"),
})
public class AppUserDetail extends AbstractBaseEntity implements SecureIdentifiable {

    @Override
    public String getSecureId() {
        return super.getSecureId();
    }

    @Override
    public Boolean getActive() {
        return super.getIsActive();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", columnDefinition = "varchar(255) default ''")
    private String name = "";

    @Column(name = "country_code")
    private String countryCode;

    @Column(name = "phone")
    private String phone;

    @Column(name = "member_bank_account")
    private String memberBankAccount;

    @Column(name = "parent_bank_account")
    private String parentBankAccount;

    @Column(name = "member_birthdate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate memberBirthdate;

    @Column(name = "parent_birthdate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate parentBirthdate;

    @Column(name = "member_cin")
    private String memberCin;

    @Column(name = "parent_cin")
    private String parentCin;

    @Column(name = "education")
    private String education;

    @Column(name = "biodata", columnDefinition = "TEXT")
    private String biodata;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private StatusType status = StatusType.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private UserType type = UserType.MEMBER_SOLITAIRE;

    @Column(name = "member_type", length = 50)
    private String memberType;

    @Column(name = "approved_by")
    private String approvedBy;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "avatar", columnDefinition = "text")
    private String avatar;

    @Column(name = "cover", columnDefinition = "text")
    private String cover;

    @Column(name = "user_as")
    private String userAs;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branchCode;

    @Column(name = "pic_name")
    private String picName;

    @Column(name = "orders")
    private Integer orders;

}
