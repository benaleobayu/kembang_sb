package com.bca.byc.entity.auth;

import com.bca.byc.entity.AbstractBaseEntityTimestamp;
import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.Post;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "post_shared")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostShared implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "from_user_id")
    private AppUser fromUser;

    @ManyToOne
    @JoinColumn(name = "to_user_id")
    private AppUser toUser;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

}
