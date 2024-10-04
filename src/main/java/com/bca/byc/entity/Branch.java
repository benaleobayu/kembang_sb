package com.bca.byc.entity;

import com.bca.byc.entity.impl.SecureIdentifiable;
import com.bca.byc.entity.impl.AttrIdentificable;
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
@Table(name = "branch", uniqueConstraints = {
    @UniqueConstraint(name = "uk_branch_seq_id", columnNames = "secure_id")
})
public class Branch extends AbstractBaseEntityCms implements SecureIdentifiable, AttrIdentificable {

    @Override
    public String getSecureId() {
        return super.getSecureId();
    }

    @Override
    public Boolean getActive() {
        return super.getIsActive();
    }


    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name = "";

    @Column(name = "address")
    private String address = "";

    @Column(name = "phone")
    private String phone = "";

    // relation
    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToOne
    @JoinColumn(name = "kanwil_id")
    private Kanwil kanwil;

    // by ?
    @ManyToOne
    @JoinColumn(name = "updated_by")
    private AppAdmin updatedBy;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private AppAdmin createdBy;
}
