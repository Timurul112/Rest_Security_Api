package com.example.rest_security_api.service;

import com.example.rest_security_api.dto.FileDto;
import com.example.rest_security_api.entity.*;
import com.example.rest_security_api.mapper.FileReadMapper;
import com.example.rest_security_api.repository.FileRepository;
import com.example.rest_security_api.util.EventUserUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {

    public static String BUCKET_NAME = "timurul112bucket";


    @Mock
    private S3Service s3Service;
    @Mock
    private FileRepository fileRepository;
    @Mock
    private FileReadMapper fileReadMapper;
    @Mock
    private EventService eventService;
    @Mock
    private EventUserUtil eventUserUtil;

    @InjectMocks
    private FileService fileService;


    @Test
    void getById() {
        Integer fileId = 1;
        File file = File.builder()
                .id(fileId)
                .name("file_name")
                .location("location")
                .status(Status.ACTIVE)
                .createdBy("user")
                .build();
        Optional<File> optionalFile = Optional.of(file);
        FileDto fileDto = FileDto.builder()
                .name("file_name")
                .location("location")
                .status(Status.ACTIVE)
                .createdBy("user")
                .build();
        Optional<FileDto> optionalFileDto = Optional.of(fileDto);

        doReturn(optionalFile).when(fileRepository).findById(fileId);
        doReturn(fileDto).when(fileReadMapper).mapToDto(file);


        Optional<FileDto> actual = fileService.getById(fileId);


        assertThat(actual).isPresent();
        assertThat(actual).isEqualTo(optionalFileDto);
    }

    @Test
    void getAll() {
        ArrayList<String> listFileMetaData = new ArrayList<>();
        listFileMetaData.add("meta_data_file1");
        listFileMetaData.add("meta_data_file2");

        doReturn(listFileMetaData).when(s3Service).getListFiles();

        List<String> actual = fileService.getAll();

        assertThat(actual).isNotEmpty();
        assertThat(actual).hasSize(listFileMetaData.size());
    }


    @Test
    void uploadFileInS3() {
        String fileName = "file_name";
        String fileContent = "content";
        String username = "username";
        User user = User.builder()
                .username(username)
                .password("password")
                .role(Role.USER)
                .status(Status.ACTIVE)
                .build();
        File file = File.builder()
                .status(Status.ACTIVE)
                .name(fileName)
                .location("location")
                .createdBy("user")
                .build();

        Event event = Event.builder()
                .user(user)
                .file(file)
                .typeOperation(Operation.CREATION)
                .build();


        doReturn(event).when(eventUserUtil).getEventForDelete(username, fileName);

        fileService.uploadFileInS3(fileName, fileContent, username);


        verify(eventService, times(1)).save(event);
        verify(s3Service, times(1)).uploadFile(BUCKET_NAME, fileName, fileContent);
    }

    @Test
    void deleteOwnFile() {
        String fileName = "file_name";
        String username = "username";
        User user = User.builder()
                .username(username)
                .password("password")
                .role(Role.USER)
                .status(Status.ACTIVE)
                .build();
        File file = File.builder()
                .status(Status.ACTIVE)
                .name(fileName)
                .location("location")
                .createdBy(username)
                .build();

        Event event = Event.builder()
                .user(user)
                .file(file)
                .typeOperation(Operation.CREATION)
                .build();

        Optional<File> optionalFile = Optional.of(file);

        doReturn(optionalFile).when(fileRepository).getByName(fileName);
        doReturn(event).when(eventUserUtil).getEventForDelete(file, username);

        fileService.deleteOwnFile(fileName, username);

        verify(eventService, times(1)).save(event);
        verify(s3Service, times(1)).deleteFile(BUCKET_NAME, fileName);
    }

    @Test
    void deleteFileByName() {

        String fileName = "file_name";
        String username = "username";
        User user = User.builder()
                .username(username)
                .password("password")
                .role(Role.USER)
                .status(Status.ACTIVE)
                .build();
        File file = File.builder()
                .status(Status.ACTIVE)
                .name(fileName)
                .location("location")
                .createdBy(username)
                .build();

        Event event = Event.builder()
                .user(user)
                .file(file)
                .typeOperation(Operation.CREATION)
                .build();

        Optional<File> optionalFile = Optional.of(file);


        doReturn(optionalFile).when(fileRepository).getByName(fileName);
        doReturn(event).when(eventUserUtil).getEventForDelete(file, username);

        fileService.deleteFileByName(fileName, username);

        verify(eventService, times(1)).save(event);
        verify(s3Service, times(1)).deleteFile(BUCKET_NAME, fileName);
    }


    @Test
    void downloadOwnFile() throws IOException {
        String fileName = "file_name";
        String username = "username";
        String result = "content";
        User user = User.builder()
                .username(username)
                .password("password")
                .role(Role.USER)
                .status(Status.ACTIVE)
                .build();
        File file = File.builder()
                .status(Status.ACTIVE)
                .name(fileName)
                .location("location")
                .createdBy(username)
                .build();

        Event event = Event.builder()
                .user(user)
                .file(file)
                .typeOperation(Operation.CREATION)
                .build();

        Optional<File> optionalFile = Optional.of(file);

        doReturn(optionalFile).when(fileRepository).getByName(fileName);
        doReturn(event).when(eventUserUtil).getEventForDownloadFile(file, username);
        doReturn(result).when(s3Service).downloadFile(fileName);

        String actual = fileService.downloadOwnFile(username, fileName);


        verify(eventService, times(1)).save(event);
        assertThat(actual).isNotEmpty();
        assertThat(actual).isEqualTo(result);
    }

    @Test
    void downloadFileByName() throws IOException {
        String fileName = "file_name";
        String username = "username";
        String result = "content";
        User user = User.builder()
                .username(username)
                .password("password")
                .role(Role.USER)
                .status(Status.ACTIVE)
                .build();
        File file = File.builder()
                .status(Status.ACTIVE)
                .name(fileName)
                .location("location")
                .createdBy(username)
                .build();

        Event event = Event.builder()
                .user(user)
                .file(file)
                .typeOperation(Operation.CREATION)
                .build();

        Optional<File> optionalFile = Optional.of(file);

        doReturn(optionalFile).when(fileRepository).getByName(fileName);
        doReturn(event).when(eventUserUtil).getEventForDownloadFile(file, username);
        doReturn(result).when(s3Service).downloadFile(fileName);

        String actual = fileService.downloadFileByName(username, fileName);

        assertThat(actual).isNotEmpty();
        assertThat(actual).isEqualTo(result);
        verify(eventService, times(1)).save(event);

    }

    @Test
    void updateOwnFile() {

        String fileName = "file_name";
        String username = "username";
        String updateFileContent = "update_content";

        User user = User.builder()
                .username(username)
                .password("password")
                .role(Role.USER)
                .status(Status.ACTIVE)
                .build();
        File file = File.builder()
                .status(Status.ACTIVE)
                .name(fileName)
                .location("location")
                .createdBy(username)
                .build();

        Event event = Event.builder()
                .user(user)
                .file(file)
                .typeOperation(Operation.CREATION)
                .build();
        Optional<File> optionalFile = Optional.of(file);


        doReturn(optionalFile).when(fileRepository).getByName(fileName);
        doReturn(event).when(eventUserUtil).getEventForUpdateFile(file, username);

        fileService.updateOwnFile(updateFileContent, username, fileName);

        verify(s3Service, times(1)).uploadFile(BUCKET_NAME, fileName, updateFileContent);
    }

    @Test
    void updateFileByName() {
        String fileName = "file_name";
        String username = "username";
        String updateFileContent = "update_content";

        User user = User.builder()
                .username(username)
                .password("password")
                .role(Role.USER)
                .status(Status.ACTIVE)
                .build();
        File file = File.builder()
                .status(Status.ACTIVE)
                .name(fileName)
                .location("location")
                .createdBy(username)
                .build();

        Event event = Event.builder()
                .user(user)
                .file(file)
                .typeOperation(Operation.CREATION)
                .build();
        Optional<File> optionalFile = Optional.of(file);

        doReturn(optionalFile).when(fileRepository).getByName(fileName);
        doReturn(event).when(eventUserUtil).getEventForUpdateFile(file, username);

        fileService.updateFileByName(updateFileContent, username, fileName);

        verify(eventService, times(1)).save(event);
        verify(s3Service, times(1)).uploadFile(BUCKET_NAME, fileName, updateFileContent);
    }
}