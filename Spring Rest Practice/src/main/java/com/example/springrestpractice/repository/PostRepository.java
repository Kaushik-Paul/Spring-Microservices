package com.example.springrestpractice.repository;

import com.example.springrestpractice.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {

    List<Post> findByUserId(Integer id);

}
