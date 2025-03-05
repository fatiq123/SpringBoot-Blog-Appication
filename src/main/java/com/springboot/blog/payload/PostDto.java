package com.springboot.blog.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Schema(
        description = "PostDto Model Information"
)
@Data
public class PostDto {

    private Long id;


    @Schema(
            description = "Blog Post Title"
    )
    // title should not be empty or null
    // title should have at least 2 characters
    @NotEmpty
    @Size(min = 2, message = "Post title should have at least 2 characters.")
    private String title;


    @Schema(
            description = "Blog Post Description"
    )
    // description should not be empty or null
    // description should have at least 10 characters
    @NotEmpty
    @Size(min = 10, message = "Post description should have at least 10 characters.")
    private String description;


    @Schema(
            description = "Blog Post Content"
    )
    // post content should not be null or empty
    @NotEmpty
    private String content;
    private Set<CommentDto> comments = new HashSet<>();


    @Schema(
            description = "Blog Post Category."
    )
    private Long categoryId;
}
