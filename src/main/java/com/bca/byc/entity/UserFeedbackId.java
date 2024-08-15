package com.bca.byc.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class UserFeedbackId implements Serializable {
    private Long userId;
    private Long feedbackCategoryId;

}
