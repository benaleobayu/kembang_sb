package com.bca.byc.entity;

import com.bca.byc.entity.impl.SecureIdentifiable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "post_category", indexes = {@Index(name = "idx_post_category_secure_id", columnList = "secure_id")})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostCategory extends AbstractBaseEntityCms implements SecureIdentifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "icon", columnDefinition = "text")
    private String icon;

    @Column(name = "name", columnDefinition = "varchar(255) default ''")
    private String name;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private AppAdmin createdBy;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    private AppAdmin updatedBy;

    @Override
    public String getSecureId() {
        return super.getSecureId();
    }

    @Override
    public Boolean getIsActive() {
        return super.getIsActive();
    }
}
