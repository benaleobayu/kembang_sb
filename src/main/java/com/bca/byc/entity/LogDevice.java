package com.bca.byc.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "log_device")
public class LogDevice extends AbstractBaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "device_id")
    private String deviceId;

    @Column(name = "version")
    private String version;

    @Column(name = "ip_address")
    private String ipAddress;

}
