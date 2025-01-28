package com.springboot.blog.controller;

import com.springboot.blog.payload.PostDto;
import com.springboot.blog.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    // Service layer dependency for handling post operations
    private final PostService postService;

    /**
     * Constructor injection of PostService dependency
     *
     * @param postService The PostService instance to be injected
     */
    public PostController(PostService postService) {
        this.postService = postService;
    }

    /**
     * Creates a new blog post
     *
     * @param postDto The post data transfer object containing post details
     * @return ResponseEntity containing created PostDto and HTTP 201 status
     */
    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto) {
        return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);
    }

    /**
     * Retrieves all blog posts
     *
     * @return List of PostDto objects representing all posts
     */
    @GetMapping
    public List<PostDto> getAllPosts() {
        return postService.getAllPosts();
    }

    /**
     * Retrieves a specific post by its ID
     *
     * @param id The ID of the post to retrieve
     * @return ResponseEntity containing PostDto if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    /**
     * Updates an existing post
     *
     * @param postDto The updated post data
     * @param id      The ID of the post to update
     * @return ResponseEntity containing updated PostDto and HTTP 200 status
     */
    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePostById(@RequestBody PostDto postDto, @PathVariable("id") long id) {
        PostDto postResponse = postService.updatePost(postDto, id);
        return new ResponseEntity<>(postResponse, HttpStatus.OK);
    }

    /**
     * Deletes a post by its ID
     *
     * @param id The ID of the post to delete
     * @return ResponseEntity containing success message and HTTP 200 status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePostById(@PathVariable("id") long id) {
        postService.deletePostById(id);
        return new ResponseEntity<>("Post entity deleted successfully.", HttpStatus.OK);
    }
}