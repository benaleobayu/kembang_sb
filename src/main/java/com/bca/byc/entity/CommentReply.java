package com.bca.byc.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "comment_reply")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentReply extends AbstractBaseEntityTimestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", columnDefinition = "text")
    private String content;

    @Column(name = "status", columnDefinition = "boolean default true")
    private Boolean status = true;

    // relations
    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @OneToMany(mappedBy = "commentReply", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LikeDislike> likeDislikes = new ArrayList<>();

}
