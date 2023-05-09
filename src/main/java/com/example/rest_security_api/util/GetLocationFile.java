package com.example.rest_security_api.util;

public class GetLocationFile {

    public static String getLocation(String bucketName, String filename) {
        return "https://" + bucketName + ".s3.amazonaws.com/" + filename;
    }
}
