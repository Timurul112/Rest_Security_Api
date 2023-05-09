package com.example.rest_security_api.rest;

import com.example.rest_security_api.dto.FileReadDto;
import com.example.rest_security_api.service.FileService;
import com.example.rest_security_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/files")
@RequiredArgsConstructor
public class FileRestControllerV1 {


    private final FileService fileService;
//    private final UserService userService;


    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<String> getAll() {
        return fileService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'USER')")
    @PostAuthorize("(hasRole('USER') and returnObject.get().createdBy == authentication.principal.username) or " +
            "hasAnyRole('MODERATOR', 'ADMIN')")
    public Optional<FileReadDto> getById(@PathVariable(name = "id") Integer fileId) {
        return fileService.getById(fileId);
    }

    @PostMapping
    public void upload(@RequestBody String fileContent, @RequestParam String username, @RequestParam String fileName) {
        fileService.uploadFile(fileName, fileContent, username);
    }







}