package com.example.rest_security_api.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.example.rest_security_api.dto.FileDto;
import com.example.rest_security_api.entity.File;
import com.example.rest_security_api.entity.Status;
import com.example.rest_security_api.repository.FileRepository;
import com.example.rest_security_api.util.GetLocationFileUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class S3ServiceTest {
    public static String BUCKET_NAME = "timurul112";


    @Mock
    private AmazonS3 s3client;


    @Mock
    private FileRepository fileRepository;

    @InjectMocks
    private S3Service s3Service;


    @Test
    void createBucket() {
        s3Service.createBucket(BUCKET_NAME);

        verify(s3client, times(1)).doesBucketExistV2(BUCKET_NAME);
        verify(s3client, times(1)).createBucket(BUCKET_NAME);
    }


    @Test
    void getListBuckets() {
        ArrayList<Bucket> buckets = new ArrayList<>();
        buckets.add(new Bucket(BUCKET_NAME));

        doReturn(buckets).when(s3client).listBuckets();

        List<Bucket> actual = s3Service.getListBuckets();

        assertThat(actual).isNotEmpty();
        assertThat(actual).hasSize(buckets.size());
    }

    @Test
    void uploadFile() {
        String fileName = "file_name";
        byte[] contentByte = "upload_test".getBytes();
        MockMultipartFile multiFile = new MockMultipartFile(fileName, contentByte);

        s3Service.uploadFile(BUCKET_NAME, fileName, multiFile);

    }

    @Test
    void getListFiles() {
        S3ObjectSummary summary1 = new S3ObjectSummary();
        summary1.setBucketName(BUCKET_NAME);
        summary1.setKey("key");
        List<S3ObjectSummary> listSummary = new ArrayList<>();
        listSummary.add(summary1);

        ObjectListing objectListing = mock(ObjectListing.class);
        String location = GetLocationFileUtil.getLocation(BUCKET_NAME, "key");
        File file = File.builder()
                .name("key1")
                .status(Status.ACTIVE)
                .location(location)
                .createdBy("user").build();
        Optional<File> optionalFile = Optional.of(file);
        doReturn(optionalFile).when(fileRepository).getByName("key");
        doReturn(objectListing).when(s3client).listObjects(BUCKET_NAME);
        doReturn(listSummary).when(objectListing).getObjectSummaries();

        List<FileDto> actual = s3Service.getListFiles(BUCKET_NAME);

        assertThat(actual).isNotEmpty();
        assertThat(actual).hasSize(listSummary.size());


    }

    @Test
    void downloadFile() throws IOException {
        String expectedContent = "test content";
        byte[] expectedBytes = expectedContent.getBytes(StandardCharsets.UTF_8);
        S3Object object = mock(S3Object.class);
        S3ObjectInputStream objectContent = new S3ObjectInputStream(new ByteArrayInputStream(expectedBytes), null);

        doReturn(object).when(s3client).getObject(BUCKET_NAME, "file-key");
        doReturn(objectContent).when(object).getObjectContent();


        String content = s3Service.downloadFile("file-key", BUCKET_NAME);
        String encodeExpectedResult = Arrays.toString(expectedContent.getBytes());

        assertThat(content).isNotEmpty();
        assertThat(content).isEqualTo(encodeExpectedResult);
    }

    @Test
    void deleteFile() {
        String key = "file_name";

        s3Service.deleteFile(BUCKET_NAME, key);

        verify(s3client).deleteObject(BUCKET_NAME, key);
    }
}