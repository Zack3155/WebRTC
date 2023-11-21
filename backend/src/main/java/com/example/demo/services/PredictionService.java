package com.example.demo.services;

import org.springframework.http.ResponseEntity;

public interface PredictionService {
    ResponseEntity<String> validate(byte[] data);
}
