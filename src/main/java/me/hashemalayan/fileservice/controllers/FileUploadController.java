package me.hashemalayan.fileservice.controllers;

import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1/")
@AllArgsConstructor
public class FileUploadController {

    private MinioClient minioClient;


    @PostMapping("/upload")
    ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) throws
            ServerException,
            InsufficientDataException,
            ErrorResponseException,
            IOException,
            NoSuchAlgorithmException,
            InvalidKeyException,
            InvalidResponseException,
            XmlParserException,
            InternalException {

        var mimeType = file.getContentType();

        if (mimeType == null || !mimeType.equals("video/mp4")) {
            return new ResponseEntity<>("Unsupported file type", HttpStatus.BAD_REQUEST);
        }

        final var bucketName = "videos";
        final var originalFilename = file.getOriginalFilename();
        final var objectName = UUID.randomUUID() + "-" + originalFilename;
        final var found = minioClient.bucketExists(
                BucketExistsArgs.builder()
                        .bucket(bucketName)
                        .build()
        );

        if (!found) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        } else {
            System.out.println("Bucket '" + bucketName + "' already exists.");
        }


        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build());


        var url = minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(bucketName)
                        .object(objectName)
                        .expiry(1, TimeUnit.HOURS)
                        .build()
        );

        return ResponseEntity.ok(url);
    }
}
