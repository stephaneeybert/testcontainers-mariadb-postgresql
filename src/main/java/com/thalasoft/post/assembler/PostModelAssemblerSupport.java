package com.thalasoft.post.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.thalasoft.post.PostController;
import com.thalasoft.post.entity.Post;
import com.thalasoft.post.model.PostModel;
import com.thalasoft.post.service.ModelService;

@Component
public class PostModelAssemblerSupport extends RepresentationModelAssemblerSupport<Post, PostModel> {

    private final ModelService modelService;

    public PostModelAssemblerSupport(ModelService postService) {
        super(PostController.class, PostModel.class);
        this.modelService = postService;
    }

    @NonNull
    @Override
    public PostModel toModel(@NonNull Post entity) {
        PostModel model = createModelWithId(entity.getId(), entity);
        model.add(linkTo(methodOn(PostController.class)
                .findById(entity.getId()))
                .withSelfRel());
        BeanUtils.copyProperties(modelService.fromPost(entity), model);
        return model;
    }

    @NonNull
    @Override
    public CollectionModel<PostModel> toCollectionModel(@NonNull Iterable<? extends Post> entities) {
        CollectionModel<PostModel> postModels = super.toCollectionModel(entities);
        postModels.add(linkTo(methodOn(PostController.class).findAll()).withSelfRel());
        return postModels;
    }
}
