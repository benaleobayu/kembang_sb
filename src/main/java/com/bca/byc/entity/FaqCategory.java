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
@Table(name = "faq_category", indexes = {@Index(name = "idx_faq_category_secure_id", columnList = "secure_id", unique = true)})
public class FaqCategory extends AbstractBaseEntityCms implements SecureIdentifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "orders", columnDefinition = "int default 1")
    private Integer orders = 1;

    @OneToMany(mappedBy = "faqCategoryId")
    private List<Faq> faqs;

    @Override
    public Boolean getIsActive() {
        return super.getIsActive();
    }

    @Override
    public String getSecureId() {
        return super.getSecureId();
    }
}