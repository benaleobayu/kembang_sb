package com.bca.byc.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostStats {
    @Column(name = "likes_count")
    private Long likesCount = 0L;

    @Column(name = "shares_count")
    private Long sharesCount = 0L;

    @Column(name = "comments_count")
    private Long commentsCount = 0L;
}
