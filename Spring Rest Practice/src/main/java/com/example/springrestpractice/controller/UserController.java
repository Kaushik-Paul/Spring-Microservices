package com.example.springrestpractice.controller;

import com.example.springrestpractice.entity.Post;
import com.example.springrestpractice.entity.User;
import com.example.springrestpractice.exceptions.PostNotFoundException;
import com.example.springrestpractice.exceptions.UserNotFoundException;
import com.example.springrestpractice.service.UserService;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity<MappingJacksonValue> getAllUsers() {
        List<User> allUsers = userService.getAllUsers();

        // Dynamically filtering Json Response
        SimpleBeanPropertyFilter simpleBeanPropertyFilter = SimpleBeanPropertyFilter.serializeAllExcept("posts");
        FilterProvider filters = new SimpleFilterProvider().addFilter("UserFilter", simpleBeanPropertyFilter);

        MappingJacksonValue mapping = new MappingJacksonValue(allUsers);
        mapping.setFilters(filters);

        return new ResponseEntity<>(mapping, HttpStatus.OK);
    }

//    @GetMapping("/users/{id}")
//    public EntityModel<ResponseEntity<User>> getSingleUser(@PathVariable int id) {
//        User user = userService.getUser(id);
//        if (user == null) {
//            throw new UserNotFoundException("User not found with id-" + id);
//        }
//
//        EntityModel<ResponseEntity<User>> model = EntityModel.of(new ResponseEntity<>(user, HttpStatus.OK));
//
//        // Build a link for getAllUsers method using HATEOAS
//        WebMvcLinkBuilder linkToUsers = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllUsers());
//
//        model.add(linkToUsers.withRel("all-users"));
//        return model;
//    }

    @GetMapping("/users/{id}")
    public ResponseEntity<MappingJacksonValue> getSingleUser(@PathVariable int id) {
        User user = userService.getUser(id);
        if (user == null) {
            throw new UserNotFoundException("User not found with id-" + id);
        }

        // Dynamically filtering Json Response
        SimpleBeanPropertyFilter simpleBeanPropertyFilter = SimpleBeanPropertyFilter.serializeAllExcept();
        FilterProvider filters = new SimpleFilterProvider().addFilter("UserFilter", simpleBeanPropertyFilter);

        MappingJacksonValue mapping = new MappingJacksonValue(user);
        mapping.setFilters(filters);
        return new ResponseEntity<>(mapping, HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<User> saveUser(@Valid @RequestBody User user) {
        return new ResponseEntity<>(userService.saveUser(user), HttpStatus.CREATED);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable int id) {
        if (userService.deleteUser(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        throw new UserNotFoundException("User not found with id: " + id);
    }

    @GetMapping("/users/{user_id}/posts")
    public ResponseEntity<List<Post>> getAllPosts(@PathVariable int user_id) {
        List<Post> posts = userService.getAllPosts(user_id);
        if (posts == null) {
            throw new UserNotFoundException("User not found with id: " + user_id);
        }
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/users/{user_id}/posts/{post_id}")
    public ResponseEntity<Post> getSinglePost(@PathVariable int user_id, @PathVariable int post_id) {
        Post post = userService.getSinglePost(user_id, post_id);
        if (post == null) {
            throw new PostNotFoundException("Post not available for either user_id " + user_id + " or post_id " + post_id);
        }
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @PostMapping("/users/{user_id}/posts")
    public ResponseEntity<Post> addPost(@RequestBody Post requestPost, @PathVariable int user_id) {
        Post post = userService.addPost(user_id, requestPost);
        if (post == null) {
            throw new UserNotFoundException("User not available for user_id " + user_id);
        }
        return new ResponseEntity<>(post, HttpStatus.CREATED);
    }

    @DeleteMapping("/users/{user_id}/posts/{post_id}")
    public ResponseEntity<Object> deletePost(@PathVariable int user_id, @PathVariable int post_id) {
        if (!userService.deletePost(user_id, post_id)) {
            throw new PostNotFoundException("Post Not Found with id: " + post_id);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
