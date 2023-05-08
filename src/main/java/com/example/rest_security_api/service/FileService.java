package com.example.rest_security_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FileService {

    public static String BUCKET_NAME = "timurul112bucket";

    private final S3Service s3Service;

    private final UserService userService;


    public String getById(String fileKey) {
        return s3Service.getFileMetadata(fileKey);
    }

    public List<String> getAll() {
        return s3Service.getListFiles();
    }

    public void uploadFile(String key, InputStream inputStream, Integer userId) {
        s3Service.uploadFile(BUCKET_NAME, key, inputStream);
    }







}
