package com.example.rest_security_api.util;

import com.example.rest_security_api.entity.Event;
import com.example.rest_security_api.entity.File;
import com.example.rest_security_api.entity.Status;
import com.example.rest_security_api.entity.User;
import com.example.rest_security_api.repository.EventRepository;
import com.example.rest_security_api.service.FileService;
import com.example.rest_security_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventUserUtil {

    private final UserService userService;
    private final FileService fileService;

    public Event getEventAndUpdateFileKey(File file, String username) {
        User user = userService.getByUsername(username);
        user.getFileKeys().remove(file.getName());
        return Event.builder()
                .file(file)
                .user(user)
                .status(Status.ACTIVE)
                .build();
    }


    public Event getEventAndUpdateFileKey(String username, String fileName) {
        User user = userService.getByUsername(username);
        File file = fileService.saveFileInDataBase(username, fileName);
        user.getFileKeys().add(fileName);
        return Event.builder()
                .file(file)
                .user(user)
                .status(Status.ACTIVE)
                .build();
    }


}
