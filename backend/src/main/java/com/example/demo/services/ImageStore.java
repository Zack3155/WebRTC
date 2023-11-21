package com.example.demo.services;

public interface ImageStore {
    void saveImage(String name, byte[] data) throws Exception;
}
