package com.bca.byc.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "app_user_attribute")
public class AppUserAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean isVerified = false;

    private Boolean isApproved = false;

    private Boolean isRejected = false;

    private Boolean isSuspended = false;

    private Boolean isDeleted = false;

    private Boolean isRecommended = false;

    public AppUserAttribute(Long appUserAttribute) {
    }
}
