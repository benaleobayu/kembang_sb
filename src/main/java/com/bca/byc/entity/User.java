package com.bca.byc.entity;

import com.bca.byc.validation.AgeRange;
import com.bca.byc.validation.PhoneNumberValidation;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "users")
public class User extends AbstractBaseEntity {

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "email", length = 50, nullable = false, unique = true)
    private String email;

    @PhoneNumberValidation
    @Column(name = "phone", length = 16)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, columnDefinition = "varchar(255) default 'member'")
    private UserType type = UserType.MEMBER;

    @Column(name = "member_type", length = 50)
    private String memberType;

    // member data
    @Column(name = "member_bank_account", length = 20)
    private String memberBankAccount;

    @Column(name = "member_cin", length = 20)
    private String memberCin;

    @AgeRange
    @Column(name = "member_birthdate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate memberBirthdate;
    // ---

    // child data
    @Column(name = "child_bank_account", length = 20)
    private String childBankAccount;

    @Column(name = "child_cin", length = 20)
    private String childCin;

    @AgeRange
    @Column(name = "child_birthdate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate childBirthdate;
    // ---

    @Column(name = "education", length = 50)
    private String education;

    @Column(name = "biodata", columnDefinition = "text")
    private String biodata;

    private String rank;

    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "last_token", columnDefinition = "text")
    private String lastToken;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status", nullable = false, columnDefinition = "int default 0")
    private StatusType status = StatusType.PENDING;

    @Column(name = "count_reject", nullable = false, columnDefinition = "int default 0")
    private Integer countReject = 0;

    @Column(name = "is_suspended", nullable = false, columnDefinition = "boolean default false")
    private Boolean isSuspended = false;

    @Column(name = "is_deleted", nullable = false, columnDefinition = "boolean default false")
    private Boolean isDeleted = false;

    // file
    @Column(name = "avatar", columnDefinition = "text")
    private String avatar;

    @Column(name = "cover", columnDefinition = "text")
    private String cover;

    // details
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserAttributes userAttributes;

    // relation
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Business> businesses = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserHasFeedback> feedbacks = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserHasExpect> userHasExpects = new ArrayList<>();

    // many to one
    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToOne
    @JoinColumn(name = "lob_id", referencedColumnName = "id")
    private LineOfBusinessCategory lineOfBusiness;

    // follow and followers
    @ManyToMany
    @JoinTable(name = "user_followers",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "follower_id"))
    private List<User> follows = new ArrayList<>();

    @ManyToMany(mappedBy = "follows")
    private List<User> followers = new ArrayList<>();

}
