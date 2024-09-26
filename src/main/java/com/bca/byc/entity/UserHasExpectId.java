package com.bca.byc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserHasExpectId implements Serializable {

    private Long userId;

    private Long expectCategoryId;

    private Long expectItemId;
}
