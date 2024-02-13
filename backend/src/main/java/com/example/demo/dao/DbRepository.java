package com.example.demo.dao;

import java.io.IOException;

import com.example.demo.entities.Image;
import com.example.demo.entities.User;

public interface DbRepository {
  int saveUser(User user);

  int saveImage(Image image);

  User getUser(String userId);

  Image getImage(String ImageId, String userId);

  void saveImageToFile(byte[] image, String imageName) throws IOException;
}
