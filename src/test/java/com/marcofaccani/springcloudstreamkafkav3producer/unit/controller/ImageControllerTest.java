package com.marcofaccani.springcloudstreamkafkav3producer.unit.controller;

import com.marcofaccani.springcloudstreamkafkav3producer.controller.ImageController;
import com.marcofaccani.springcloudstreamkafkav3producer.service.interfaces.ImageService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ImageController.class)
public class ImageControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  ImageService imageService;

  @Nested
  class UploadImage {

    @Test
    @SneakyThrows
    void shouldReturn200AndInvokeUploadImage() {
      final var file = new MockMultipartFile("file", new byte[]{});

      mockMvc.perform(multipart("/image").file(file))
          .andExpect(status().isOk());

      verify(imageService).uploadImage(file);
    }

  }
}
