package com.example.rest_security_api.rest;


import com.example.rest_security_api.dto.UserCreateEditDto;
import com.example.rest_security_api.dto.UserReadDto;
import com.example.rest_security_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserRestControllerV1 {


    private final UserService userService;



    @GetMapping
    @PreAuthorize("hasAnyAuthority('MODERATOR', 'ADMIN')")
    public List<UserReadDto> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('MODERATOR', 'ADMIN')")
    public UserReadDto getById(@PathVariable Integer id) {
        return userService.getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR', 'USER')")
    public UserReadDto create(@RequestBody UserCreateEditDto user) {
        return userService.create(user);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR', 'USER')")
    public UserReadDto update(@PathVariable Integer id, @RequestBody UserCreateEditDto user) {
        return userService.update(id, user).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Integer id) {
        if (!userService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}