package com.blog.demo.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String postContent;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name="user_entity_id")
    //@NotNull
    private User user;

    @ToString.Exclude
    @OneToMany
    @JoinColumn(name = "post")
    private Set<Comment> comments = new HashSet<>();

    public Post(String postContent, User user, Set<Comment> comments) {
        this.postContent = postContent;
        this.user = user;
        this.comments = comments;
    }

    public Post(String postContent) {
        this.postContent = postContent;
    }



}