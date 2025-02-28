package com.example.todojpa.controller;

import com.example.todojpa.annotation.LoginUser;
import com.example.todojpa.annotation.LoginUserDto;
import com.example.todojpa.dto.request.user.UserCreateRequestDto;
import com.example.todojpa.dto.request.user.UserDeleteRequestDto;
import com.example.todojpa.dto.request.user.UserUpdateRequestDto;
import com.example.todojpa.dto.response.user.UserResponse;
import com.example.todojpa.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
                                           @RequestBody @Valid UserUpdateRequestDto requestDto,
                                           @LoginUser LoginUserDto loginUser) {
        userService.updateUser(id, loginUser.getUserId(), requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id,
                                           @RequestBody @Valid UserDeleteRequestDto requestDto,
                                           @LoginUser LoginUserDto loginUser) {
        userService.deleteUser(id, loginUser.getUserId(), requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}
