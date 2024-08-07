package com.in28minutes.rest.webservices.restfulwebservices.user;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.in28minutes.rest.webservices.restfulwebservices.jpa.PostRepository;
import com.in28minutes.rest.webservices.restfulwebservices.jpa.UserRepository;

import jakarta.validation.Valid;

@RestController
public class UserJPAResource {

		
	private UserRepository userRepository ;
	private PostRepository postRepository;

	public UserJPAResource(UserRepository userRepository,PostRepository postRepository) {
		this.userRepository = userRepository;
		this.postRepository = postRepository;
	}

	// GET/users
	@GetMapping("/jpa/users")
	public List<User> retrieveAllUsers() {
		return userRepository.findAll();
	}

	// GET/users/1
	@GetMapping("/jpa/user/{id}")
	public EntityModel<User> retrieveUsers(@PathVariable int id) {
		Optional<User> user = userRepository.findById(id);
		if (user.isEmpty())
			throw new UserNotFoundException("id :" + id);
		EntityModel<User> entityModel = EntityModel.of(user.get());
		
		WebMvcLinkBuilder link =  linkTo(methodOn(this.getClass()).retrieveAllUsers());
		entityModel.add(link.withRel("all-users"));
		
		return entityModel;
	}
	
	
	//for HATEOAS
	/*
	 * @GetMapping("/user/{id}") public EntityModel<User>
	 * retrieveUsers(@PathVariable int id) { User user =
	 * userDaoService.findUserById(id); if (user == null) throw new
	 * UserNotFoundException("id :" + id);
	 * 
	 * EntityModel<User> entityModel = EntityModel.of(user); WebMvcLinkBuilder link
	 * = linkTo(methodOn(this.getClass()).retrieveAllUsers());
	 * entityModel.add(link.withRel("all-users"));
	 * 
	 * return entityModel; }
	 */

	@DeleteMapping("/jpa/user/{id}")
	public void deleteUsers(@PathVariable int id) {
		userRepository.deleteById(id);

	}
	
	@GetMapping("/jpa/user/{id}/posts")
	public List<Post> retrievePostsForUser(@PathVariable int id) {
		Optional<User> user = userRepository.findById(id);
		
		if(user.isEmpty())
			throw new UserNotFoundException("id : "+id);
		return user.get().getPosts();

	}

	// POST/user
	@PostMapping("/jpa/create")
	public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
		User saveUser = userRepository.save(user);
		// user/4 => /users, user.getId
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(saveUser.getId())
				.toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PostMapping("/jpa/user/{id}/posts")
	public ResponseEntity<Object> createPostsForUser(@PathVariable int id,@Valid @RequestBody Post post) {
		Optional<User> user = userRepository.findById(id);
		
		if(user.isEmpty())
			throw new UserNotFoundException("id : "+id);
		
		post.setUser(user.get());
		
		Post postSave = postRepository.save(post);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(postSave.getId())
				.toUri();
		return ResponseEntity.created(location).build();

	}
}
