package com.bca.byc.entity;

import com.bca.byc.entity.impl.SecureIdentifiable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "blacklist_keyword")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BlacklistKeyword extends AbstractBaseEntityCms implements SecureIdentifiable {

    @Override
    public Long getId() {
        return super.getId();
    }

    @Override
    public String getSecureId() {
        return super.getSecureId();
    }
    @Override
    public Boolean getActive() {
        return super.getIsActive();
    }

    @Column(name = "keyword")
    private String keyword;

    @Column(name = "orders", columnDefinition = "int default 1")
    private Integer orders = 1;

}
