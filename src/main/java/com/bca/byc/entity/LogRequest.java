package com.bca.byc.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "log_requests")
public class LogRequest extends AbstractBaseEntityCms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private AppUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private AppAdmin admin;

    @Column(name = "model_id", nullable = false)
    private Long modelId;

    @Column(name = "model_type", nullable = false)
    private String modelType;

    @Column(name = "log_from", nullable = false)
    private String logFrom;

    @Column(name = "log_to", nullable = false)
    private String logTo;

    @Column(name = "note")
    private String note;

}
