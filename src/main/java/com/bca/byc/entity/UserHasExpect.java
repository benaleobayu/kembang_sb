package com.bca.byc.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name= "user_has_expect")
public class UserHasExpect {

    @EmbeddedId
    private UserHasExpectId id = new UserHasExpectId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("expectCategoryId")
    @JoinColumn(name = "expect_category_id")
    private ExpectCategory expectCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("expectItemId")
    @JoinColumn(name = "expect_item_id")
    private ExpectItem expectItem;

    @Column(name = "other_expect")
    private String otherExpect;

    @Column(name = "other_expect_item")
    private String otherExpectItem;

}
