package com.example.rest_security_api.rest;


import com.example.rest_security_api.dto.UserCreateDto;
import com.example.rest_security_api.dto.UserReadDto;
import com.example.rest_security_api.dto.UserUpdateDto;
import com.example.rest_security_api.service.UserService;
import com.example.rest_security_api.util.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostAuthorize;
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
    @PreAuthorize("hasAnyAuthority('MODERATOR', 'ADMIN', 'USER')")
    @PostAuthorize("(hasAuthority('USER') and returnObject.username == authentication.principal.username)" +
            "or hasAnyAuthority('ADMIN', 'MODERATOR')")
    public UserReadDto getById(@PathVariable Integer id) {
        return userService.getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserReadDto create(@RequestBody UserCreateDto user) {
        return userService.create(user);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR', 'USER')")
    public UserReadDto updateWithoutPassword(@PathVariable(name = "id") Integer changeId, @RequestBody UserUpdateDto userUpdate) {
        String authority = AuthenticationUtil.getAuthority();
        String authUsername = AuthenticationUtil.getUsername();
        if (authority.equals("USER")) {
            return userService.updateOwnUser(userUpdate, changeId, authUsername);
        }
        if (authority.equals("MODERATOR") || authority.equals("ADMIN")) {
            return userService.updateById(changeId, userUpdate);
        } else
            throw new RuntimeException("Enter correct data");
    }


//    @PatchMapping("/reset-password/{id}")
//    public void deleteByIdAs() {
//
//    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Integer id) {
        if (!userService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
