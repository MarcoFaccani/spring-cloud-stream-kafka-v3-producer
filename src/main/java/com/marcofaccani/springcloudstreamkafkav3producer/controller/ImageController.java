package com.marcofaccani.springcloudstreamkafkav3producer.controller;

import com.marcofaccani.springcloudstreamkafkav3producer.service.interfaces.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageController {

  private final ImageService imageService;

  @PostMapping
  public ResponseEntity<HttpStatus> uploadImage(MultipartFile file) {
    imageService.uploadImage(file);
    return ResponseEntity.ok().build();
  }

}
