package com.example.demo.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.stereotype.Component;

@Component
public class FileImageStore implements ImageStore {
    public void saveImage(String imageName, byte[] image) throws IOException {
        System.out.println("saveImageToFile");
        File path = new File("./images/");
        if (!path.exists()) {
            path.mkdir();
        }
        Files.write(new File("./images/" + imageName).toPath(), image);
        System.out.println("Create Dir");
    }

}
