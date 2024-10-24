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
public class LogRequest extends AbstractBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "model_id", nullable = false)
    private Long modelId;

    @Column(name = "model_type", nullable = false)
    private String modelType;

    @Column(name = "log_from", nullable = false)
    private String logFrom;

    @Column(name = "log_to", nullable = false)
    private String logTo;

    @Column(name = "created_by")
    private String nameCreatedBy;

    @Column(name = "created_by_id")
    private Long idCreatedBy;

    @Column(name = "note")
    private String note;

}
