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
@Table(name = "log_pre_register")
public class PreRegisterLog extends AbstractBaseEntityTimestamp{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pre_register_id")
    private PreRegister preRegister;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private LogStatus status = LogStatus.UNDEFINED;

    @Column(name = "message", columnDefinition = "text")
    private String message;

    @Column(name = "updated_by")
    private String updatedBy;

}
