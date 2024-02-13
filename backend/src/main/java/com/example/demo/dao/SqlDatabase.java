package com.example.demo.dao;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Image;
import com.example.demo.entities.User;

@Repository
public class SqlDatabase implements DbRepository {
  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Override
  public int saveUser(User user) {

    return jdbcTemplate.update(
        "insert into MyUsers (UserID, UserName, email) values(?,?,?)",
        user.getUserId(), user.getUserName(), user.getEmail());

  }

  @Override
  public int saveImage(Image image) {

    return jdbcTemplate.update(
        "insert into MyImages (ImageID, UserID, ImageData) values(?,?,?)",
        image.getImageId(), image.getUserId(), image.getImageData());

  }

  @Override
  public User getUser(String userId) {
    String sql = "SELECT * FROM MyUsers WHERE UserID = ?";
    User target = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new User(
        rs.getString("UserID"),
        rs.getString("UserName"),
        rs.getString("Email")), new Object[] { userId });
    return target;
  }

  @Override
  public Image getImage(String imageId, String userId) {
    String sql = "SELECT * FROM MyImages WHERE UserID = ? AND ImageID = ?";
    Image target = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new Image(
        rs.getString("ImageID"),
        rs.getString("UserID"),
        rs.getBytes("ImageData")), new Object[] { imageId, userId });
    return target;

  }

  @Override
  public void saveImageToFile(byte[] image, String imageName) throws IOException {
    File dir = new File("/images/");
    if (!dir.exists()) {
      dir.mkdirs();
    }
    Files.write(new File("/images/" + imageName).toPath(), image);
  }

}
