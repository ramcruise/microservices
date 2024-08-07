package com.in28minutes.rest.webservices.restfulwebservices.user;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import jakarta.validation.Valid;

@RestController
public class UserResource {

	@Autowired
	private UserDaoService userDaoService;

	public UserResource(UserDaoService userDaoService) {
		this.userDaoService = userDaoService;
	}

	// GET/users
	@GetMapping("/users")
	public List<User> retrieveAllUsers() {
		return userDaoService.findAllUsers();
	}

	// GET/users/1
	@GetMapping("/user/{id}")
	public User retrieveUsers(@PathVariable int id) {
		User user = userDaoService.findUserById(id);
		if (user == null)
			throw new UserNotFoundException("id :" + id);
		
		return user;
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

	@DeleteMapping("/user/{id}")
	public void deleteUsers(@PathVariable int id) {
		userDaoService.deleteUserById(id);

	}

	// POST/user
	@PostMapping("/create")
	public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
		User saveUser = userDaoService.save(user);
		// user/4 => /users, user.getId
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(saveUser.getId())
				.toUri();
		return ResponseEntity.created(location).build();
	}
}
