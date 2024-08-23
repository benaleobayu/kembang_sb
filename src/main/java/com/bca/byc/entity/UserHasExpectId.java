package com.bca.byc.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserHasExpectId implements Serializable {

    private Long userId;

    private Long expectCategoryId;

    private Long expectItemId;
}
