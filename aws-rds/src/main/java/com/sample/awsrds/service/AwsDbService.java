package com.sample.awsrds.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rds.RdsClient;
import software.amazon.awssdk.services.rds.model.CreateDbInstanceRequest;
import software.amazon.awssdk.services.rds.model.CreateDbInstanceResponse;
import software.amazon.awssdk.services.rds.model.DBInstance;
import software.amazon.awssdk.services.rds.model.DescribeDbInstancesRequest;
import software.amazon.awssdk.services.rds.model.DescribeDbInstancesResponse;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

@Slf4j
@Service
public class AwsDbService {

    @Value("${aws.db-instance-identifier}")
    private String dbInstanceIdentifier;
    @Value("${aws.aws-secrets-manager-name}")
    private String awsSecretsManagerName;
    @Value("${aws.region}")
    private String awsRegion;
    @Value("${aws.db-name}")
    private String dbName;

    public String createDatabaseInstance() throws Exception {

        Region region = Region.of(awsRegion);
        SecretsManagerClient secretClient = SecretsManagerClient.builder()
            .region(region)
            .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
            .build();
        GetSecretValueRequest valueRequest = GetSecretValueRequest.builder()
            .secretId(awsSecretsManagerName)
            .build();
        GetSecretValueResponse valueResponse = secretClient.getSecretValue(valueRequest);
        String jsonString = String.valueOf(valueResponse.secretString());

        ObjectMapper mapper = new ObjectMapper();
        TypeReference<HashMap<String, String>> typeRef = new TypeReference<HashMap<String, String>>() {};
        HashMap<String, String> masterCredentials = mapper.readValue(jsonString, typeRef);

        CreateDbInstanceRequest instanceRequest = CreateDbInstanceRequest.builder()
                .dbInstanceIdentifier(dbInstanceIdentifier)
                .allocatedStorage(100)
                .dbName(dbName)
                .engine("mysql")
                .dbInstanceClass("db.m4.large")
                .engineVersion("8.0")
                .storageType("standard")
                .masterUsername(masterCredentials.get("username"))
                .masterUserPassword(masterCredentials.get("password"))
                .build();

        RdsClient rdsClient = RdsClient.builder().region(region).build();
        CreateDbInstanceResponse response = rdsClient.createDBInstance(instanceRequest);
        log.debug("Database creation status {}", response.dbInstance().dbInstanceStatus());


        DescribeDbInstancesRequest dbInstancesRequest = DescribeDbInstancesRequest.builder()
            .dbInstanceIdentifier(dbInstanceIdentifier)
            .build();
        boolean instanceReady = false;
        while (!instanceReady) {
            DescribeDbInstancesResponse dbInstancesResponse = rdsClient.describeDBInstances(dbInstancesRequest);
            List<DBInstance> instanceList = dbInstancesResponse.dbInstances();
            for (DBInstance instance : instanceList) {
                String instanceReadyStr = instance.dbInstanceStatus();
                if (instanceReadyStr.contains("available"))
                    instanceReady = true;
                else {                    
                    Thread.sleep(2000);
                }
            }
        }

        rdsClient.close();
        return "Database created successfully";
    }
}
