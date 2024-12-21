package com.kembang.entity;


import com.kembang.entity.impl.SecureIdentifiable;
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
@Table(name = "documents", indexes = {@Index(name = "idx_documents_secure_id", columnList = "secure_id", unique = true)})
public class Documents extends AbstractBaseEntity implements SecureIdentifiable {
    @Column(name = "name")
    private String name;

    @Column(name = "identity", unique = true)
    private String identity;

    @Column(name = "url_file", columnDefinition = "text")
    private String urlFile;

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
}
