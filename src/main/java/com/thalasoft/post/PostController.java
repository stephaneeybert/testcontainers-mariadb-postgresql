package com.thalasoft.post;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.thalasoft.post.assembler.PostModelAssembler;
import com.thalasoft.post.entity.Post;
import com.thalasoft.post.model.PostModel;
import com.thalasoft.post.service.ModelService;
import com.thalasoft.post.service.PostService;
import com.thalasoft.post.utils.RESTUtils;

import jakarta.validation.Valid;

@RestController
@RequestMapping(RESTUtils.SLASH + RESTUtils.API + RESTUtils.SLASH + RESTUtils.POSTS)
public class PostController {

    private static final Logger log = LoggerFactory.getLogger(PostController.class);
    private final ModelService modelService;
    private final PostService postService;
    private final PostModelAssembler postModelAssembler;

    public PostController(ModelService modelService, PostService postService,
            PostModelAssembler postModelAssembler) {
        this.modelService = modelService;
        this.postService = postService;
        this.postModelAssembler = postModelAssembler;
    }

    // @GetMapping("")
    // public ResponseEntity<PagedModel<PostModel>> findAll(
    // @PageableDefault(sort = { "orderedOn" }, direction = Sort.Direction.ASC)
    // Pageable pageable, Sort sort,
    // PagedResourcesAssembler<PostModel> pagedResourcesAssembler,
    // UriComponentsBuilder builder) {
    // sort = RESTUtils.stripColumnsFromSorting(sort, nonSortableColumns);
    // postService.addSortToPageable(pageable, sort);
    // Page<Post> posts = postService.findAll(pageable);
    // PagedModel<PostModel> pagedPostModels =
    // pagedResourcesAssembler.toModel(posts, postModelAssembler);
    // UriComponentsBuilder uriComponentsBuilder = builder.path(RESTUtils.SLASH);
    // modelService.addPageableToUri(uriComponentsBuilder, pageable);
    // HttpHeaders responseHeaders = new HttpHeaders();
    // responseHeaders.setLocation(uriComponentsBuilder.buildAndExpand().toUri());
    // return
    // ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body(pagedPostModels);
    // }

    @GetMapping("")
    public ResponseEntity<List<PostModel>> findAll() {
        return ResponseEntity.ok().body(modelService.toPostModels(postService.findAll()));
    }

    @GetMapping("/hateoas")
    public ResponseEntity<CollectionModel<PostModel>> hateoasFindAll() {
        return ResponseEntity.ok().body(postModelAssembler.toCollectionModel(postService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostModel> findById(@PathVariable final Long id) {
        return Optional.ofNullable(postModelAssembler.toModel(postService.findById(id)))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PostModel> save(@RequestBody @Valid final PostModel postModel, UriComponentsBuilder builder) {
        Post post = postService.add(modelService.toPost(postModel));
        URI location = builder.path(RESTUtils.SLASH + RESTUtils.POSTS + RESTUtils.SLASH + "{id}")
                .buildAndExpand(post.getId()).toUri();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(location);
        return ResponseEntity.status(HttpStatus.CREATED).headers(responseHeaders)
                .body(postModelAssembler.toModel(post));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PostModel> update(@PathVariable final Long id, @RequestBody @Valid final PostModel postModel,
            UriComponentsBuilder builder) {
        Post post = postService.update(id, modelService.toPost(postModel));
        PostModel updatedPostModel = postModelAssembler.toModel(post);
        URI location = builder.path(RESTUtils.SLASH + RESTUtils.POSTS + RESTUtils.SLASH + "{id}")
                .buildAndExpand(post.getId()).toUri();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(location);
        return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body(updatedPostModel);
    }

    @PatchMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PostModel> partialUpdate(@PathVariable Long id, @Valid @RequestBody PostModel postModel,
            UriComponentsBuilder builder) {
        Post post = postService.partialUpdate(id, modelService.toPost(postModel));
        PostModel updatedUserModel = postModelAssembler.toModel(post);
        URI location = builder.path(RESTUtils.SLASH + RESTUtils.POSTS + RESTUtils.SLASH + "{id}")
                .buildAndExpand(post.getId()).toUri();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(location);
        return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body(updatedUserModel);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        postService.delete(id);
    }
}
