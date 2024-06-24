package com.thalasoft.post;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.thalasoft.post.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p")
    public Stream<Post> streamAll(Pageable page);

    public Optional<Post> findByTitle(String title);


    public Optional<Post> findByIsbn(String isbn);

}
