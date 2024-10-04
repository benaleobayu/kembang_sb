package com.bca.byc.entity;

import com.bca.byc.enums.LogStatus;
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
@Table(name = "log_user_management")
public class UserManagementLog extends AbstractBaseEntityTimestamp{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type")
    private String type;

    @ManyToOne
    @JoinColumn(name = "pre_register_id")
    private PreRegister preRegister;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private LogStatus status = LogStatus.UNDEFINED;

    @Column(name = "message", columnDefinition = "text")
    private String message;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    private AppAdmin updatedBy;

}
