package com.sample.awss3.controller;

import org.springframework.web.bind.annotation.RestController;

import com.sample.awss3.service.S3BucketOperations;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RestController("myS3Client")
@RequiredArgsConstructor
public class S3Controller {

    private final S3BucketOperations s3BucketOperations;

    @PutMapping("createNewBucket")
    public String createNewBucket() {
        String bucketName = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        return s3BucketOperations.createNewBucket(bucketName);
    }

    @PostMapping("deleteBucket/{bucketName}")
    public String deleteBucket(@PathVariable String bucketName) {
        return s3BucketOperations.deleteBucket(bucketName);
    }

    @GetMapping("/getAllBuckets")
    public List<String> getAllBuckets() {
        return s3BucketOperations.getAllBuckets();
    }

    @PostMapping("/putNewObjectIntoBucket/{bucketName}")
    public String putNewObjectIntoBucket(@PathVariable String bucketName, @RequestBody String content) {
        return s3BucketOperations.putNewObjectIntoBucket(bucketName, content, "objectKey");
    }

    @PostMapping("/deleteObjectFromBucket/{bucketName}")
    public String deleteObjectFromBucket(@PathVariable String bucketName, @RequestParam String objectName) {
        return s3BucketOperations.deleteObjectFromBucket(bucketName, objectName);
    }

    @PostMapping("/copyObjectToNewBucket")
    public String copyObjectToNewBucket(@RequestParam String objectKey,@RequestParam String fromBucket,@RequestParam String toBucket) {
        return s3BucketOperations.copyObjectToNewBucket(objectKey, fromBucket, toBucket);
    }
}
