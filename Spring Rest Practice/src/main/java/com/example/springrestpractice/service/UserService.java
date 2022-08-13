package com.example.springrestpractice.service;

import com.example.springrestpractice.entity.Post;
import com.example.springrestpractice.entity.User;
import com.example.springrestpractice.repository.PostRepository;
import com.example.springrestpractice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUser(int id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public boolean deleteUser(int id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.delete(user.get());
            return true;
        }
        return false;
    }

    public List<Post> getAllPosts(int user_id) {
        Optional<User> optionalUser = userRepository.findById(user_id);
        if (optionalUser.isEmpty()) {
            return null;
        }
        return postRepository.findByUserId(user_id);
    }

    public Post getSinglePost(int user_id, int post_id) {
        Optional<User> optionalUser = userRepository.findById(user_id);
        if (optionalUser.isEmpty()) {
            return null;
        }
        Optional<Post> optionalPost = postRepository.findById(post_id);
        if (optionalPost.isEmpty()) {
            return null;
        }
        return optionalPost.get();
    }

    public Post addPost(int user_id, Post post) {
        Optional<User> optionalUser = userRepository.findById(user_id);
        if (optionalUser.isEmpty()) {
            return null;
        }
        post.setUser(optionalUser.get());
        return postRepository.save(post);
    }

    public boolean deletePost(int user_id, int post_id) {
        Optional<User> optionalUser = userRepository.findById(user_id);
        if (optionalUser.isEmpty()) {
            return false;
        }
        Optional<Post> optionalPost = postRepository.findById(post_id);
        if (optionalPost.isEmpty()) {
            return false;
        }
        postRepository.deleteById(post_id);
        return true;
    }
}
