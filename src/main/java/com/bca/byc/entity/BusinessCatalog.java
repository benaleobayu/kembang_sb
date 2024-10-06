package com.bca.byc.entity;

import com.bca.byc.entity.impl.SecureIdentifiable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import jakarta.persistence.*;


@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "business_catalog")
public class BusinessCatalog  extends AbstractBaseEntity implements SecureIdentifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "business_secure_id", nullable = false, referencedColumnName = "secure_id")
    private Business business;

    @Column(nullable = false, length = 100)
    private String title;  // Title of the catalog entry

    @Column(length = 255)
    private String image;  // URL or path to the image

    @Column(columnDefinition = "TEXT")
    private String description;  // Description of the catalog entry

    @Override
    public String getSecureId() {
        return super.getSecureId();
    }

    @Override
    public Boolean getActive() {
        return super.getIsActive();
    }
}
