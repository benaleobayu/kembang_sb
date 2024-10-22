package com.bca.byc.entity;

import com.bca.byc.enums.ActionType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "log_request")
public class LogRequest extends AbstractBaseEntityCms implements Modelable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private AppUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private AppAdmin admin;

    @Column(name = "modelable_id", nullable = false)
    private Long modelableId;

    @Column(name = "modelable_type", nullable = false)
    private String modelableType;

    @Column(name = "from", nullable = false)
    private String from;

    @Column(name = "to", nullable = false)
    private String to;

    @Column(name = "note")
    private String note;

    @Override
    public Long getId() {
        return id;  // Implement the method from Modelable interface
    }
}
