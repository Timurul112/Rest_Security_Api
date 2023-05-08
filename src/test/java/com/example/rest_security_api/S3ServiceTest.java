package com.example.rest_security_api;

import com.amazonaws.services.s3.model.Bucket;
import com.example.rest_security_api.service.S3Service;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class S3ServiceTest {


    @Autowired
    private S3Service s3Service;

    @Test
    public void createBucketTest() {
        s3Service.createBucket();
    }

    @Test
    public void getListBuckets() {
        List<Bucket> listBuckets = s3Service.getListBuckets();
        assertThat(listBuckets).isNotEmpty();
        assertThat(listBuckets).hasSize(1);
        System.out.println(listBuckets.get(0).getName());
    }


}
