package com.example.todojpa.controller;

import com.example.todojpa.dto.request.UserCreateRequestDto;
import com.example.todojpa.dto.request.UserDeleteRequestDto;
import com.example.todojpa.dto.request.UserUpdateRequestDto;
import com.example.todojpa.dto.response.UserResponse;
import com.example.todojpa.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public ResponseEntity<UserResponse> search(@RequestParam(required = false) String name) {
        UserResponse res = userService.search(name);

        return ResponseEntity.ok(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable Long id) {
        UserResponse res = userService.findById(id);

        return ResponseEntity.ok(res);
    }

    @PostMapping("")
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserCreateRequestDto requestDto) {
        UserResponse user = userService.createUser(requestDto);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable("id") Long id,
                                           @RequestBody @Valid UserUpdateRequestDto requestDto) {
        userService.updateUser(id, requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping("/{id}/delete")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id,
                                           @RequestBody @Valid UserDeleteRequestDto requestDto) {
        userService.deleteUser(id, requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}
