package com.springboot.blog.payload;

import lombok.Data;

import java.util.List;

// this class is used for pagination and sorting functionality
@Data
public class PostResponse {

    private List<PostDto> content;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;

}
