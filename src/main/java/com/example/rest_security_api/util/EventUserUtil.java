package com.example.rest_security_api.util;

import com.example.rest_security_api.entity.*;
import com.example.rest_security_api.repository.FileRepository;
import com.example.rest_security_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventUserUtil {
    public static String BUCKET_NAME = "timurul112bucket";

    private final UserService userService;
    private final FileRepository fileRepository;


    public Event getEventAndUpdateFileKey(File file, String username) {
        User user = userService.getByUsername(username);
        user.getFileKeys().remove(file.getName());
        return Event.builder()
                .file(file)
                .user(user)
                .typeOperation(Operation.DELETED)
                .build();
    }


    public Event getEventAndUpdateFileKey(String username, String fileName) {
        User user = userService.getByUsername(username);
        String location = GetLocationFileUtil.getLocation(BUCKET_NAME, fileName);
        File savedFile = File.builder()
                .createdBy(username)
                .status(Status.ACTIVE)
                .name(fileName)
                .location(location)
                .build();
        fileRepository.save(savedFile);
        user.getFileKeys().add(fileName);
        return Event.builder()
                .file(savedFile)
                .user(user)
                .typeOperation(Operation.CREATE)
                .build();
    }

    public Event getEventForDownloadFile(File file, String username) {
        User user = userService.getByUsername(username);
        return Event.builder()
                .user(user)
                .file(file)
                .typeOperation(Operation.DOWNLOADED)
                .build();
    }
  public Event getEventForUpdateFile(File file, String username) {
        User user = userService.getByUsername(username);
        return Event.builder()
                .user(user)
                .file(file)
                .typeOperation(Operation.UPDATE)
                .build();
    }



}
