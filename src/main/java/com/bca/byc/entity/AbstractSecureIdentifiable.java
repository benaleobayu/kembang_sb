package com.bca.byc.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Data
@MappedSuperclass
public class AbstractSecureIdentifiable implements Serializable {

    /**
     *
     */
    @Serial
    private static final long serialVersionUID = -2119574796403647424L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "secure_id", nullable = false, unique = true, columnDefinition = "char(36) default gen_random_uuid()")
    private String secureId = UUID.randomUUID().toString();

    @Column(name = "is_active", columnDefinition = "boolean default true")
    private Boolean active = true;
}
