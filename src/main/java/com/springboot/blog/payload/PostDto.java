package com.springboot.blog.payload;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
public class PostDto {

    private long id;
    private String title;
    private String description;
    private String content;
    private Set<CommentDto> comments = new HashSet<>();
}
