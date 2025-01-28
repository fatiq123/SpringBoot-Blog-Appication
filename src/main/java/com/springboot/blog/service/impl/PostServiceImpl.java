package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    // Repository injection for database operations
    private PostRepository postRepository;

    // Constructor injection of PostRepository
    @Autowired
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // Creates a new blog post
    // @param postDto Data transfer object containing post details
    // @return PostDto object of the created post
    @Override
    public PostDto createPost(PostDto postDto) {
        // Convert DTO to entity
        Post post = mapToEntity(postDto);

        // Save the post to database
        Post newPost = postRepository.save(post);

        // Convert saved entity back to DTO
        PostDto postResponse = mapToDto(newPost);

        return postResponse;
    }

    // Retrieves all blog posts from the database
    // @return List of all posts as DTOs
    @Override
    public List<PostDto> getAllPosts() {
        // Get all posts from database
        List<Post> posts = postRepository.findAll();
        // Convert each post to DTO and collect to list
        return posts.stream().map(post -> mapToDto(post)).collect(Collectors.toList());
    }

    // Finds a specific post by its ID
    // @param id The ID of the post to find
    // @return PostDto of the found post
    // @throws ResourceNotFoundException if post not found
    @Override
    public PostDto getPostById(long id) {
        // Find post or throw exception if not found
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        return mapToDto(post);
    }

    // Updates an existing post
    // @param postDto New post data
    // @param id ID of post to update
    // @return Updated PostDto
    // @throws ResourceNotFoundException if post not found
    @Override
    public PostDto updatePost(PostDto postDto, long id) {
        // Find post or throw exception if not found
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));

        // Update post fields
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());

        // Save updated post
        Post updatedPost = postRepository.save(post);
        return mapToDto(updatedPost);
    }

    // Deletes a post by its ID
    // @param id ID of post to delete
    // @throws ResourceNotFoundException if post not found
    @Override
    public void deletePostById(long id) {
        // Find post or throw exception if not found
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        postRepository.delete(post);
    }

    // Utility method to convert Post entity to PostDto
    // @param post Post entity to convert
    // @return PostDto representation of the entity
    private PostDto mapToDto(Post post) {
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setDescription(post.getDescription());
        postDto.setContent(post.getContent());
        return postDto;
    }

    // Utility method to convert PostDto to Post entity
    // @param postDto PostDto to convert
    // @return Post entity representation of the DTO
    private Post mapToEntity(PostDto postDto) {
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        return post;
    }
}
