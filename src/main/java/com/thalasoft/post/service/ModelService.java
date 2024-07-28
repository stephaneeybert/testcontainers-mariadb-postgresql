package com.thalasoft.post.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.thalasoft.post.PostRepository;
import com.thalasoft.post.entity.Post;
import com.thalasoft.post.model.PostModel;
import com.thalasoft.post.utils.RESTUtils;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ModelService {

    private final PostRepository postRepository;

    public ModelService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post toPost(PostModel postModel) {
        Post post = null;
        if (postModel.getId() == null) {
            post = new Post();
        } else {
            try {
                Optional<Post> optional = postRepository.findById(postModel.getId());
                post = optional.orElseGet(Post::new);
            } catch (EntityNotFoundException e) {
                post = new Post();
            }
        }
        post.setUserId(postModel.getUserId());
        post.setTitle(postModel.getTitle());
        post.setBody(postModel.getBody());
        post.setIsbn(postModel.getIsbn());
        return post;
    }

    public List<Post> toPosts(List<PostModel> postModels) {
        return postModels.stream().map(this::toPost).toList();
    }

    public PostModel fromPost(Post post) {
        PostModel postModel = new PostModel();
        postModel.setId(post.getId());
        postModel.setUserId(post.getUserId());
        postModel.setTitle(post.getTitle());
        postModel.setBody(post.getBody());
        postModel.setIsbn(post.getIsbn());
        return postModel;
    }

    public List<PostModel> toPostModels(List<Post> posts) {
        return posts.stream().map(this::fromPost).toList();
    }

    public void addPageableToUri(UriComponentsBuilder uriComponentsBuilder, Pageable pageable) {
        uriComponentsBuilder.queryParam("page", pageable.getPageNumber()).queryParam("size", pageable.getPageSize());
        for (Sort.Order order : pageable.getSort()) {
            uriComponentsBuilder.queryParam("sort", order.getProperty())
                    .queryParam(order.getProperty() + RESTUtils.PAGEABLE_SORT_SUFFIX,
                            order.getDirection().name());
        }
    }

    public void addSortToPageable(Pageable page, Sort sort) {
        page.getSort().and(sort);
    }
}
