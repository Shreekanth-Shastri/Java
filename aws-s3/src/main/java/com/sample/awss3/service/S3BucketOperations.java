package com.sample.awss3.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.CopyObjectResponse;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.DeleteBucketRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectsRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.ListBucketsRequest;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3BucketOperations {

    private final S3Client s3Client;

    public String createNewBucket(String bucketName) {
        CreateBucketRequest bucketRequest = CreateBucketRequest.builder()
            .bucket(bucketName)
            .build();
        s3Client.createBucket(bucketRequest);
        HeadBucketRequest bucketRequestWait = HeadBucketRequest.builder().bucket(bucketName).build();
        return s3Client.waiter().waitUntilBucketExists(bucketRequestWait).matched().response().map(response -> {
            String message = String.format("bucket %s created successfully, response: %s", bucketName, response);
            log.debug("{}", message);
            return message;
        }).orElse(null);
    }

    public String deleteBucket(String bucketName) {
        DeleteBucketRequest deleteBucketRequest = DeleteBucketRequest.builder()
            .bucket(bucketName)
            .build();
        s3Client.deleteBucket(deleteBucketRequest);
        HeadBucketRequest bucketRequestWait = HeadBucketRequest.builder().bucket(bucketName).build();
        return s3Client.waiter().waitUntilBucketNotExists(bucketRequestWait).matched().response().map(response -> {
            String message = String.format("bucket %s deleted successfully, response: %s", bucketName, response);
            log.debug("{}", message);
            return message;
        }).orElse(null);
    }

    public List<String> getAllBuckets() {
        ListBucketsRequest listBucketsRequest = ListBucketsRequest.builder().build();
        ListBucketsResponse listBucketsResponse = s3Client.listBuckets(listBucketsRequest);
        List<String> bucketList = new ArrayList<>();
        listBucketsResponse.buckets().stream().forEach(bucket -> bucketList.add(bucket.name()));
        return bucketList;
    }

    public byte[] getObjectFromBucket(String bucketName, String objectKey) {
        GetObjectRequest objectRequest = GetObjectRequest.builder()
            .key(objectKey)
            .bucket(bucketName)
            .build();
        return s3Client.getObjectAsBytes(objectRequest).asByteArray();
    }

    public String getContentType( String bucketName, String objectKey) {
        String contentType = null;
        try {
            HeadObjectRequest objectRequest = HeadObjectRequest.builder()
                .key(objectKey)
                .bucket(bucketName)
                .build();
            HeadObjectResponse objectHead = s3Client.headObject(objectRequest);
            contentType = objectHead.contentType();
        } catch (S3Exception ex) {
            log.error(ex.awsErrorDetails().errorMessage());
        }
        return contentType;
    }

    public String putNewObjectIntoBucket(String bucketName, String content, String objectKey) {
        String responseMessage = null;
        try {
            Map<String, String> metadata = new HashMap<>();
            metadata.put("key", "value");
            PutObjectRequest putOb = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .metadata(metadata)
                .build();
            s3Client.putObject(putOb,RequestBody.fromString(content));
            responseMessage = String.format("Successfully copied %s into bucket %s", objectKey, bucketName);
            log.debug("{}", responseMessage);
        } catch (S3Exception ex) {
            log.error(ex.getMessage());
        }
        return responseMessage;
    }

    public String deleteObjectFromBucket(String bucketName, String objectName){
        String responseMessage = null;
        List<ObjectIdentifier> objectsToDelete = new ArrayList<>();
        objectsToDelete.add(ObjectIdentifier.builder().key(objectName).build());
        try {
            DeleteObjectsRequest dor = DeleteObjectsRequest.builder()
                .bucket(bucketName)
                .delete(del -> del.objects(objectsToDelete))
                .build();
            s3Client.deleteObjects(dor);
        } catch (S3Exception ex) {
            responseMessage = ex.getMessage();
            log.error(responseMessage);
        }
        return responseMessage;
    }

    public String copyObjectToNewBucket(String objectKey, String fromBucket, String toBucket){
        String responseMessage = null;
        CopyObjectRequest copyReq = CopyObjectRequest.builder()
            .sourceKey(objectKey)
            .sourceBucket(fromBucket)            
            .destinationKey(objectKey)
            .destinationBucket(toBucket)
            .build();
        try {
            CopyObjectResponse copyRes = s3Client.copyObject(copyReq);
            responseMessage = copyRes.copyObjectResult().toString();
        } catch (S3Exception ex) {
            responseMessage = ex.getMessage();
            log.error(responseMessage);
        }
        return responseMessage;
    }
}
