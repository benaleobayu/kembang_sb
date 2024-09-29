package com.bca.byc.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "faq", indexes = {@Index(name = "idx_faq_secure_id", columnList = "secure_id", unique = true)})
public class Faq extends AbstractBaseEntityCms implements SecureIdentifiable {

    @Column(name = "question", nullable = false)
    private String question;

    @Column(name = "answer", nullable = false, columnDefinition = "text")
    private String answer;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "orders", columnDefinition = "int default 1")
    private Integer orders;

    @ManyToOne
    @JoinColumn(name = "faq_category_id", nullable = false)
    private FaqCategory faqCategoryId;

    @Override
    public Boolean getActive() {
        return super.isActive();
    }

    @Override
    public String getSecureId() {
        return super.getSecureId();
    }
}