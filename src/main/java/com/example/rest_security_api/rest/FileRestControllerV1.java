package com.example.rest_security_api.rest;

import com.example.rest_security_api.dto.FileDto;
import com.example.rest_security_api.service.FileService;
import com.example.rest_security_api.util.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

/**
 * Получение файла по id , или всех файлов - это означает получение метаинформации по файлу. Если нужно получить
 * сам файл - то это метод .download(только по ID, заружать разом все файлы-нельзя).
 */

@RestController
@RequestMapping("api/v1/files")
@RequiredArgsConstructor
public class FileRestControllerV1 {


    private final FileService fileService;


    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    public ResponseEntity<List<FileDto>> getAll(@RequestParam String bucketName) {
        List<FileDto> listFileDto = fileService.getAll(bucketName);
        return ResponseEntity.ok(listFileDto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR', 'USER')")
    @PostAuthorize("(hasAuthority('USER') and returnObject.get().createdBy == authentication.principal.username) or " +
            "hasAnyAuthority('MODERATOR', 'ADMIN')")
    public ResponseEntity<Optional<FileDto>> getById(@PathVariable(name = "id") Integer fileId) {
        Optional<FileDto> optionalFileDto = fileService.getById(fileId);
        return ResponseEntity.ok(optionalFileDto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR', 'USER')")
    public void upload(@RequestParam MultipartFile file, @RequestParam String fileName, @RequestParam String bucketName) {
        String username = AuthenticationUtil.getUsername();
        fileService.uploadFileInS3(fileName, file, username, bucketName);
    }


    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR', 'USER')")
    public void deleteByName(@RequestParam String fileName, @RequestParam String bucketName) {
        String authority = AuthenticationUtil.getAuthority();
        String username = AuthenticationUtil.getUsername();
        if (authority.equals("USER")) {
            fileService.deleteOwnFile(fileName, username, bucketName);
        } else if (authority.equals("ADMIN") || authority.equals("MODERATOR")) {
            fileService.deleteFileByName(fileName, username, bucketName);
        }
    }


    @GetMapping("/download/{fileName}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR', 'USER')")
    public void downloadFile(HttpServletResponse response, @PathVariable String fileName,
                             @RequestParam String bucketName) throws IOException {
        String authority = AuthenticationUtil.getAuthority();
        String username = AuthenticationUtil.getUsername();
        response.setContentType("application/json");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=%s".formatted(fileName));
        if (authority.equals("USER")) {
            String result = fileService.downloadOwnFile(username, fileName,bucketName);
            try (PrintWriter writer = response.getWriter()) {
                writer.write(result);
            }
        } else if (authority.equals("ADMIN") || authority.equals("MODERATOR")) {
            String result = fileService.downloadFileByName(username, fileName, bucketName);
            try (PrintWriter writer = response.getWriter()) {
                writer.write(result);
            }
        }
    }

    @PutMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR', 'USER')")
    public void updateFile(@RequestBody MultipartFile file, @RequestParam String fileName, @RequestParam String bucketName) {
        String authority = AuthenticationUtil.getAuthority();
        String username = AuthenticationUtil.getUsername();
        if (authority.equals("USER")) {
            fileService.updateOwnFile(file, username, fileName, bucketName);
        } else if (authority.equals("ADMIN") || authority.equals("MODERATOR")) {
            fileService.updateFileByName(file, username, fileName, bucketName);
        }
    }
}






