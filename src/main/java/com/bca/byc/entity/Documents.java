package com.bca.byc.entity;


import com.bca.byc.entity.impl.SecureIdentifiable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "comments", indexes = {@Index(name = "idx_comments_secure_id", columnList = "secure_id", unique = true)})
public class Documents extends AbstractBaseEntityCms implements SecureIdentifiable {
    @Override
    public Long getId() {
        return super.getId();
    }

    @Override
    public String getSecureId() {
        return super.getSecureId();
    }

    @Override
    public Boolean getIsActive() {
        return super.getIsActive();
    }

    @Column(name = "name")
    private String name;

    @Column(name = "url_file", columnDefinition = "text")
    private String urlFile;
}
