package com.bca.byc.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "app_user", indexes = {@Index(name = "uk_email", columnList = "email")})
public class AppUser extends AbstractBaseEntity implements UserDetails {

    /**
     *
     */
    @Serial
    private static final long serialVersionUID = 3423593371835697230L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", columnDefinition = "varchar(255) default ''")
    private String name = "";

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password")
    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "app_user_detail_id", referencedColumnName = "id")
    private AppUserDetail appUserDetail;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "app_user_attribute_id", referencedColumnName = "id")
    private AppUserAttribute appUserAttribute;

    @Column(name = "count_reject", columnDefinition = "int default 0")
    private Integer countReject = 0;

    // relation
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Business> businesses = new ArrayList<>();
    //    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private List<UserHasFeedback> feedbacks = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserHasExpect> userHasExpects = new ArrayList<>();

    // many to one
    @ManyToOne
    @JoinColumn(name = "location_id")
    @JsonIgnore
    private Location location;


    // follow and followers
    @ManyToMany
    @JoinTable(name = "user_followers",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "follower_id"))
    private List<AppUser> follows = new ArrayList<>();

    @ManyToMany(mappedBy = "follows")
    private List<AppUser> followers = new ArrayList<>();


    /////////////////

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;

    public AppUser() {

    }

    public AppUser(Long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public AppUser(Long id, String name, String email, String password, AppUserDetail appUserDetail, AppUserAttribute appUserAttribute, Integer countReject, List<Business> businesses, List<UserHasExpect> userHasExpects, Location location, List<AppUser> follows, List<AppUser> followers, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.appUserDetail = appUserDetail;
        this.appUserAttribute = appUserAttribute;
        this.countReject = countReject;
        this.businesses = businesses;
        this.userHasExpects = userHasExpects;
        this.location = location;
        this.follows = follows;
        this.followers = followers;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return true;
    }
}
