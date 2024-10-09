package com.bca.byc.entity;

import com.bca.byc.entity.impl.SecureIdentifiable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "channel", indexes = {
    @Index(name = "idx_chanel_secure_id", columnList = "secure_id", unique = true)
})
public class Channel extends AbstractBaseEntityCms implements SecureIdentifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "orders", columnDefinition = "int default 1")
    private Integer orders;

    @Column(name = "logo", columnDefinition = "text")
    private String logo;

    @Column(name = "privacy")
    private String privacy;

    @OneToMany(mappedBy = "channel")
    private List<Post> contents;

    @Override
    public Boolean getActive() {
        return super.getIsActive();
    }

    @Override
    public String getSecureId() {
        return super.getSecureId();
    }
}