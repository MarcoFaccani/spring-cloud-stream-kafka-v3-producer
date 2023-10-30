package com.marcofaccani.springcloudstreamkafkav3producer.service;


import com.marcofaccani.springcloudstreamkafkav3producer.constant.AppBindingChannels;
import com.marcofaccani.springcloudstreamkafkav3producer.service.interfaces.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Log4j2
public class ImageServiceImpl implements ImageService {

  private final StreamBridge streamBridge;

  @Override
  @SneakyThrows
  public void uploadImage(MultipartFile file) {
    final var topic = AppBindingChannels.IMAGE_TOPIC.getValue();
    log.debug("Sending file to topic {}...", topic);
    streamBridge.send(topic, file.getBytes());
    log.info("File successfully sent");
  }
}
