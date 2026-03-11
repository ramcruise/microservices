package com.ecommerce.user.controllers;

import com.ecommerce.user.dto.UserRequest;
import com.ecommerce.user.dto.UserResponse;
import com.ecommerce.user.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Slf4j
public class UserController {


    private final UserService userService;
   // private static Logger logger = LoggerFactory.getLogger(UserController.class);


    @GetMapping()
    public ResponseEntity<List<UserResponse>> getUserList() {
        return new ResponseEntity<>(userService.fetchAllUsers(),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable String id) {
       /*  User user = userService.fetchUser(id);
         if(user==null)
             return ResponseEntity.notFound().build();
        return ResponseEntity.ok(user);*/
        log.info("Fetching user with id {}", id);
        log.trace("This is trace level - Very detailed log");
        log.debug("This Debug level - used for development dubugging");
        log.error("This is error level - Something failed");
        log.warn("This is warn level - Somthing might be wrong");

        return userService.fetchUser(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<String> createUser(@RequestBody UserRequest userRequest) {
        userService.addUser(userRequest);
        return ResponseEntity.ok("User has been added successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable String id, @RequestBody UserRequest updateUserRequest) {
        boolean user = userService.updateUser(id,updateUserRequest);
        if (!user)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok("User has been updated successfully");
    }





}
