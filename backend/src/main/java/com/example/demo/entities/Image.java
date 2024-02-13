package com.example.demo.entities;

public class Image {
  String imageId;
  String userId;
  byte[] imageData;

  public String getImageId() {
    return imageId;
  }

  public void setImageId(String imageId) {
    this.imageId = imageId;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public byte[] getImageData() {
    return imageData;
  }

  public void setImageData(byte[] imageData) {
    this.imageData = imageData;
  }

  public Image(String imageId, String userId, byte[] imageData) {
    this.imageId = imageId;
    this.userId = userId;
    this.imageData = imageData;
  }
}
