package com.example.rest_security_api.rest;

import com.example.rest_security_api.entity.File;
import com.example.rest_security_api.entity.Role;
import com.example.rest_security_api.service.FileService;
import com.example.rest_security_api.service.UserService;
import com.example.rest_security_api.util.RoleFromUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.example.rest_security_api.entity.Role.*;

@RestController
@RequestMapping("api/v1/files")
@RequiredArgsConstructor
public class FileRestControllerV1 {


    private final FileService fileService;
    private final UserService userService;


    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<String> getAll() {
        return fileService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'USER')")
    public Optional<File> getById(@PathVariable Integer id, @RequestParam Integer userId, @RequestParam String key) {


//        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        String userDetailsRole = RoleFromUserDetails.getRole(userDetails);
//        if (userDetailsRole.equals(ADMIN.name()) || userDetailsRole.equals(USER.name())) {
//            fileService.getById(key);
//        } else {
//            userService.getById()


    }
