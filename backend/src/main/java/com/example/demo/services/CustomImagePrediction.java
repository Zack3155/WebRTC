package com.example.demo.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CustomImagePrediction implements PredictionService {
    final static String PREDICTION_ENDPOINT = "https://webrtczack-prediction.cognitiveservices.azure.com/customvision/v3.0/Prediction/d0398471-9008-42fa-b35d-ce9c0c7601d2/classify/iterations/Iteration2/image";
    final static String API_KEY = "5f4ec41395b84003bf2602aee5561088";

    static RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<String> validate(byte[] data) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/octet-stream");
        headers.add("Prediction-Key", API_KEY);

        HttpEntity<byte[]> entity = new HttpEntity<>(data, headers);
        ResponseEntity<String> result = restTemplate.postForEntity(PREDICTION_ENDPOINT, entity, String.class);
        return result;
    }

}
