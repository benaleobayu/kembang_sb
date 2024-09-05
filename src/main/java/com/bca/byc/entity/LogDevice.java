package com.bca.byc.entity;

import com.bca.byc.enums.ActionType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "log_device")
public class LogDevice extends AbstractBaseEntityNoUUID {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private AppUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private AppAdmin admin;

    @Column(name = "device_id")
    private String deviceId;

    @Column(name = "version")
    private String version;

    @Column(name = "action_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ActionType actionType = ActionType.LOGIN;

    @Column(name = "ip_address")
    private String ipAddress;

}
