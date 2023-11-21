package com.example.demo.services;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.springframework.stereotype.Component;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;

@Component
public class CloudImageStore implements ImageStore {
    String CONNECTION_STRING = "DefaultEndpointsProtocol=https;AccountName=webrtc;AccountKey=iuohUdUXjhWdNt+NVtxGui4SYrhIh8efVVZfiP2xtOeMEBdWY3f0Pr/53rHXcxIHfEuaK90g0L6n+AStd01MIg==;EndpointSuffix=core.windows.net";

    public void saveImage(String imageName, byte[] image) {
        // Create a BlobServiceClient object using a connection string
        BlobServiceClient client = new BlobServiceClientBuilder().connectionString(CONNECTION_STRING).buildClient();

        // Create a unique name for the container (here it's a folder)
        String containerName = "images";

        // Create the container (if doesn't exist) and return a container client object
        BlobContainerClient blobContainerClient = client.createBlobContainerIfNotExists(containerName);

        // Get a reference to a blob (aka a file)
        BlobClient blobClient = blobContainerClient.getBlobClient(imageName);

        // Upload the blob
        // blobClient.uploadFromFile(localPath + fileName); // this is upload from local
        InputStream targetStream = new ByteArrayInputStream(image);
        blobClient.upload(targetStream);
    }

}
