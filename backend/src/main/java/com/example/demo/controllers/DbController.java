package com.example.demo.controllers;

import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dao.DbRepository;
import com.example.demo.entities.Image;
import com.example.demo.entities.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin(origins = "${FRONTEND_HOST:*}")
public class DbController {
  @Autowired
  @Qualifier("sqlDatabase")
  DbRepository sqlDatabase;

  @Autowired
  private StringRedisTemplate stringRedisTemplate;

  @RequestMapping(value = "/form", method = RequestMethod.POST)
  public ResponseEntity<String> postForm(@RequestBody String data) throws JSONException, JsonProcessingException {
    JSONObject jsonObject = new JSONObject(data);
    String name = jsonObject.getString("name");
    String email = jsonObject.getString("email");
    String base64 = jsonObject.getString("image").replace("data:image/png;base64,", "");
    byte[] rawBytes = Base64.getDecoder().decode(base64);

    UUID user_uuid = UUID.randomUUID();
    String userId = user_uuid.toString();
    UUID image_uuid = UUID.randomUUID();
    String imageId = image_uuid.toString();

    User user = new User(userId, name, email);
    Image image = new Image(imageId, userId, rawBytes);

    sqlDatabase.saveUser(user);
    sqlDatabase.saveImage(image);
    saveCache(user);

    ResponseEntity<String> result = new ResponseEntity<String>(HttpStatus.OK);

    return result;

  }

  @GetMapping("/image/{imageId}/{userId}")
  ResponseEntity<Image> getImage(@PathVariable String imageId, @PathVariable String userId) throws IOException {

    Image image = sqlDatabase.getImage(imageId, userId);

    String imageName = image.getUserId() + ".png";

    sqlDatabase.saveImageToFile(image.getImageData(), imageName);

    return new ResponseEntity<Image>(image, HttpStatus.OK);

  }

  @GetMapping("/users/{userId}")
    public ResponseEntity getUser(@PathVariable String userId){
        try {
            User user = readCache(userId);
            System.out.println(userId);
            return new ResponseEntity<User>(user, HttpStatus.OK);
        } catch (Exception e){
            System.out.println(e);
            return new ResponseEntity<String>("fail", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



  private void saveCache(User user) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(user);
    ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
    String key = user.getUserId();
    ops.set(key, json);
  }

  private User readCache(String id) throws Exception {
    ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
    User user = null;
    if (!stringRedisTemplate.hasKey(id)) {
      user = sqlDatabase.getUser(id);
      System.out.println("cache miss!");
      saveCache(user);
    } else {
      ObjectMapper mapper = new ObjectMapper();
      String json = ops.get(id);
      user = mapper.readValue(json, User.class);
      System.out.println("cache hit!");
    }
    return user;
  }

}
