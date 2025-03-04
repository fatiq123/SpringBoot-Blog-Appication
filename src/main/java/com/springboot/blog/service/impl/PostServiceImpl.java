package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Category;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.repository.CategoryRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation class for managing blog posts.
 * Handles CRUD operations and pagination for blog posts.
 */
@Service
public class PostServiceImpl implements PostService {

    /**
     * Repository for database operations on Post entities
     */
    private PostRepository postRepository;

    private ModelMapper modelMapper;

    private CategoryRepository categoryRepository;


    /**
     * Constructor for dependency injection of PostRepository
     *
     * @param postRepository Repository instance for Post entity operations
     */
    @Autowired
    public PostServiceImpl(PostRepository postRepository, ModelMapper modelMapper, CategoryRepository categoryRepository) {
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
        this.categoryRepository = categoryRepository;
    }

    /**
     * Creates a new blog post
     *
     * @param postDto Data transfer object containing post details
     * @return PostDto object of the created post
     */
    @Override
    public PostDto createPost(PostDto postDto) {

        Category category = categoryRepository.findById(postDto.getCategoryId()).orElseThrow(
                () -> new ResourceNotFoundException("Category", "id", postDto.getCategoryId())
        );

        // Convert DTO to entity
        Post post = mapToEntity(postDto);


        post.setCategory(category);

        // Save the post to database
        Post newPost = postRepository.save(post);

        // Convert saved entity back to DTO
        PostDto postResponse = mapToDto(newPost);

        return postResponse;
    }

    /**
     * Retrieves paginated and sorted list of all blog posts
     *
     * @param pageNo   Page number to retrieve (zero-based)
     * @param pageSize Number of items per page
     * @param sortBy   Field to sort by
     * @param sortDir  Sort direction (asc/desc)
     * @return PostResponse containing paginated list of posts and metadata
     */
    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Post> posts = postRepository.findAll(pageable);

        // get content for page object
        List<Post> listOfPosts = posts.getContent();

        List<PostDto> content = listOfPosts.stream().map(post -> mapToDto(post)).collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());

        return postResponse;
    }

    /**
     * Retrieves a specific post by its ID
     *
     * @param id The ID of the post to find
     * @return PostDto of the found post
     * @throws ResourceNotFoundException if post with given ID is not found
     */
    @Override
    public PostDto getPostById(long id) {
        // Find post or throw exception if not found
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        return mapToDto(post);
    }

    /**
     * Updates an existing post
     *
     * @param postDto New post data to update with
     * @param id      ID of the post to update
     * @return Updated PostDto
     * @throws ResourceNotFoundException if post with given ID is not found
     */
    @Override
    public PostDto updatePost(PostDto postDto, long id) {
        // Find post or throw exception if not found
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));

        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Category", "id", postDto.getCategoryId()));


        // Update post fields
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());

        post.setCategory(category);

        // Save updated post
        Post updatedPost = postRepository.save(post);
        return mapToDto(updatedPost);
    }

    /**
     * Deletes a post by its ID
     *
     * @param id ID of the post to delete
     * @throws ResourceNotFoundException if post with given ID is not found
     */
    @Override
    public void deletePostById(long id) {
        // Find post or throw exception if not found
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        postRepository.delete(post);
    }

    /**
     * Converts Post entity to PostDto
     *
     * @param post Post entity to convert
     * @return PostDto representation of the entity
     */
    private PostDto mapToDto(Post post) {

        PostDto postDto = modelMapper.map(post, PostDto.class);

//        PostDto postDto = new PostDto();
//        postDto.setId(post.getId());
//        postDto.setTitle(post.getTitle());
//        postDto.setDescription(post.getDescription());
//        postDto.setContent(post.getContent());
        return postDto;
    }

    /**
     * Converts PostDto to Post entity
     *
     * @param postDto PostDto to convert
     * @return Post entity representation of the DTO
     */
    private Post mapToEntity(PostDto postDto) {

        Post post = modelMapper.map(postDto, Post.class);

//        Post post = new Post();
//        post.setTitle(postDto.getTitle());
//        post.setDescription(postDto.getDescription());
//        post.setContent(postDto.getContent());
        return post;
    }


    @Override
    public List<PostDto> getPostsByCategory(Long categoryId) {

        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category", "id", categoryId)
        );

        List<Post> posts = postRepository.findByCategoryId(categoryId);

        return posts.stream()
                .map(
                        (post -> mapToDto(post)
                        )
                ).collect(Collectors.toList());

    }
}