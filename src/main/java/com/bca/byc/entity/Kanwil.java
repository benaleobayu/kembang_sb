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
@Table(name = "kanwil", uniqueConstraints = {
        @UniqueConstraint(name = "uk_kanwil_seq_id", columnNames = "secure_id")
})
public class Kanwil extends AbstractBaseEntityCms implements SecureIdentifiable {

    @Override
    public String getSecureId() {
        return super.getSecureId();
    }

    @Override
    public Boolean getActive() {
        return super.isActive();
    }

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    // relation
    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

}
