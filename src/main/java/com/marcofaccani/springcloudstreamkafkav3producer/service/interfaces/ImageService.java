package com.marcofaccani.springcloudstreamkafkav3producer.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

  void uploadImage(MultipartFile file);

}
