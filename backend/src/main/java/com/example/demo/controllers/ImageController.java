package com.example.demo.controllers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Base64;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.example.demo.services.ImageStore;
import com.example.demo.services.PredictionService;

@RestController
@CrossOrigin(origins = "${FRONTEND_HOST:*}")
public class ImageController {
    @Autowired
    // @Qualifier("fileImageStore")
    // @Qualifier("cloudImageStore")
    @Qualifier("customImageStore")
    ImageStore imageStore;

    @Autowired
    @Qualifier("customImagePrediction")
    PredictionService predictionService;

    @PostMapping("/validate")
    public ResponseEntity<String> validate(@RequestBody String data) throws IOException {
        String base64 = data.replace("data:image/png;base64,", "");
        byte[] rawBytes = Base64.getDecoder().decode(base64);
        return predictionService.validate(rawBytes);
    }

    @GetMapping("/greeting") // http://localhost:8080/greeting?name=yukewu
    public String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return "Hello " + name;
    }

    @PostMapping("/images")
    public ResponseEntity<Object> uploadImage(@RequestBody String data) throws Exception {
        System.out.println("Method Called");
        String base64 = data.replace("data:image/png;base64,", "");
        byte[] rawBytes = Base64.getDecoder().decode(base64); // string decoded to byte[]
        String imageName = UUID.randomUUID() + ".png"; // random name for image
        // imageStore.saveImage(imageName, rawBytes);
        // * Name Zack to Give a Tag to Custom Vision
        imageStore.saveImage("Zack", rawBytes);
        // saveToCloud(imageName, rawBytes);
        // CustomVision.uploadYukeImage(rawBytes); // upload to Custom Vision
        return new ResponseEntity<>("Successfully uploaded image", HttpStatus.OK);
    }
}
