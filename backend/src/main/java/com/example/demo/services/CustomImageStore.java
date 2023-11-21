package com.example.demo.services;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class CustomImageStore implements ImageStore {
    final static String PROJECT_ID = "d0398471-9008-42fa-b35d-ce9c0c7601d2";
    final static String TRAINING_ENDPOINT = "https://webrtczack.cognitiveservices.azure.com";
    final static String PREDICTION_ENDPOINT = "https://webrtczack.cognitiveservices.azure.com/customvision/v3.0/Prediction/d0398471-9008-42fa-b35d-ce9c0c7601d2/classify/iterations/Iteration1/image";
    final static String API_KEY = "90e7c3a051a5427183b9a89a60626114";

    static RestTemplate restTemplate = new RestTemplate();

    // Maps each tag to its Custom Vision tag ID
    final static public Map<String, String> tags = new HashMap<>() {
        {
            put("Zack", "9ca3e459-2fef-43e4-bde7-965d4a2dcbde");
            put("Others", "806a32d5-a4bc-46b4-a9e3-3ae664e7dc25");
        }
    };

    /** Creates a new tag to categorize data in Custom Vision */
    public static void createTag(String tagName) throws JSONException {
        // Add params to URI
        Map<String, String> params = new HashMap<>();
        params.put("endpoint", TRAINING_ENDPOINT);
        params.put("projectId", PROJECT_ID);
        params.put("name", tagName);

        String url = "{endpoint}/customvision/v3.3/Training/projects/{projectId}/tags?name={name}";
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        URI uri = builder.buildAndExpand(params).toUri();

        // Set request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Training-key", API_KEY);

        // Make request and print result of success
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.postForEntity(uri, request, String.class);
        JSONObject jsonObject = new JSONObject(response.getBody());

        System.out.println(jsonObject.getString("id") + ": " + tagName);
    }

    public void saveImage(String name, byte[] image) throws IOException {
        uploadImage(tags.get(name), image);
    }

    /** Uploads a new image to the training set and tags it accordingly */
    private void uploadImage(String tagId, byte[] fileData) throws JSONException {
        // Add params to URI
        Map<String, String> params = new HashMap<>();
        params.put("endpoint", TRAINING_ENDPOINT);
        params.put("projectId", PROJECT_ID);
        params.put("tagIds", tagId);

        String url = "{endpoint}/customvision/v3.3/training/projects/{projectId}/images?tagIds={tagIds}";
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        URI uri = builder.buildAndExpand(params).toUri();

        // Set request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.set("Training-key", API_KEY);

        // Make request and print result of success
        HttpEntity<byte[]> request = new HttpEntity<>(fileData, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(uri, request, String.class);

        System.out.println(response.getBody());
    }

    public static void main(String args[]) throws JSONException {
        createTag("Zack");
        createTag("Others");
    }

}
