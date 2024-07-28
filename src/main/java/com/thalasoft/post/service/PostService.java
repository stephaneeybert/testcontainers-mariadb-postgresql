package com.thalasoft.post.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.thalasoft.post.PostRepository;
import com.thalasoft.post.entity.Post;
import com.thalasoft.post.exception.custom.EntityAlreadyExistsException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post getReferenceById(Long id) {
        return postRepository.getReferenceById(id);
    }

    public Post findById(Long id) {
        return postRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public List<Post> findAllByIds(List<Long> ids) {
        return postRepository.findAllById(ids);
    }

    public List<Post> findAll() {
        List<Post> posts = postRepository.findAll();
        if (!posts.isEmpty()) {
            return posts;
        } else {
            throw new EntityNotFoundException();
        }
    }

    public Page<Post> findAll(Pageable page) {
        Page<Post> posts = postRepository.findAll(page);
        if (posts.getNumberOfElements() > 0) {
            return posts;
        } else {
            throw new EntityNotFoundException();
        }
    }

    public Post findByIsbn(String isbn) {
        return postRepository.findByIsbn(isbn).orElseThrow(EntityNotFoundException::new);
    }

    @Modifying
    @Transactional
    public Post add(Post post) {
        try {
            findByIsbn(post.getIsbn());
            throw new EntityAlreadyExistsException();
        } catch (EntityNotFoundException e) {
            return postRepository.save(post);
        }
    }

    @Modifying
    @Transactional
    public Post update(Long existingPostId, Post modifiedPost) {
        Post existingPost = findById(existingPostId);
        if (existingPost == null) {
            throw new EntityNotFoundException();
        } else {
            existingPost.setUserId(modifiedPost.getUserId());
            existingPost.setTitle(modifiedPost.getTitle());
            existingPost.setBody(modifiedPost.getBody());
            existingPost.setIsbn(modifiedPost.getIsbn());
            return postRepository.save(existingPost);
        }
    }

    @Modifying
    @Transactional
    public Post partialUpdate(Long existingPostId, Post modifiedPost) {
        Post existingPost = findById(existingPostId);
        if (existingPost == null) {
            throw new EntityNotFoundException();
        } else {
            existingPost.setUserId(modifiedPost.getUserId());
            if (StringUtils.hasText(modifiedPost.getTitle())) {
                existingPost.setTitle(modifiedPost.getTitle());
            }
            if (StringUtils.hasText(modifiedPost.getBody())) {
                existingPost.setBody(modifiedPost.getBody());
            }
            if (StringUtils.hasText(modifiedPost.getIsbn())) {
                existingPost.setIsbn(modifiedPost.getIsbn());
            }
            return postRepository.save(existingPost);
        }
    }

    @Modifying
    @Transactional
    public void delete(Long id) {
        Post post = findById(id);
        if (post == null) {
            throw new EntityNotFoundException();
        } else {
            postRepository.delete(post);
        }
    }
}
