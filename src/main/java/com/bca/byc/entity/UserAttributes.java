package com.bca.byc.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user_attributes")
@Data
public class UserAttributes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean isRecommended;
    private Boolean isVerified;
    private Boolean isBlocked;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
