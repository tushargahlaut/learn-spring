package com.tushar.rest.webservices.restful_web_services.user;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.tushar.rest.webservices.restful_web_services.jpa.PostRepository;
import com.tushar.rest.webservices.restful_web_services.jpa.UserRepository;

import jakarta.validation.Valid;

@RestController
public class UserJpaResource {
    private UserRepository service;
    private PostRepository postService;

    public UserJpaResource(UserRepository userDaoService, PostRepository postService) {
        this.service = userDaoService;
        this.postService = postService;
    }

    @GetMapping("/users")
    public List<User> retrieveAllUsers() {
        return service.findAll();
    }

    @GetMapping("/users/{id}")
    public Optional<User> retrieveUser(@PathVariable int id) {
        Optional<User> foundUser = service.findById(id);
        if (foundUser.isEmpty()) {
            throw new UserNotFoundException("User not Found: id-" + id);
        }
        return foundUser;
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id) {
        service.deleteById(id);

    }

    @GetMapping("/users/{id}/posts")
    public List<Post> retrievePostForUser(@PathVariable int id) {
        Optional<User> foundUser = service.findById(id);
        if (foundUser.isEmpty()) {
            throw new UserNotFoundException("User not Found: id-" + id);
        }
        return foundUser.get().getPosts();
    }

    @PostMapping("/users")
    public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
        User savedUser = service.save(user);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PostMapping("/users/{id}/posts")
    public ResponseEntity<Object> createPostForUser(@PathVariable Integer id, @Valid @RequestBody Post post) {
        Optional<User> user = service.findById(id);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User not Found: id-" + id);
        }
        post.setUser(user.get());
        Post savedPost = postService.save(post);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedPost.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/posts/{id}")
    public Optional<Post> getSinglePost(@PathVariable Integer id) {
        Optional<Post> post = postService.findById(id);
        if (post.isEmpty()) {
            throw new UserNotFoundException("Post not Found: id-" + id);
        }
        return post;
    }
}
