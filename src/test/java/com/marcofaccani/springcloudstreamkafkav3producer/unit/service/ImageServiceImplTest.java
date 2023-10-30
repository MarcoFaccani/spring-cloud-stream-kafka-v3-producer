package com.marcofaccani.springcloudstreamkafkav3producer.unit.service;

import com.marcofaccani.springcloudstreamkafkav3producer.constant.AppBindingChannels;
import com.marcofaccani.springcloudstreamkafkav3producer.service.ImageServiceImpl;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class ImageServiceImplTest {

  @InjectMocks
  ImageServiceImpl underTest;

  @Mock
  StreamBridge streamBridge;

  @Nested
  class UploadImage {

    final String topicName = AppBindingChannels.IMAGE_TOPIC.getValue();

    @Test
    void shouldProduceEventToGivenTopic() {
      final var file = new MockMultipartFile("file", new byte[]{});

      assertDoesNotThrow( () -> underTest.uploadImage(file));
      verify(streamBridge).send(eq(topicName), any());
    }

    @Test
    void shouldPropagateException() {
      final var errMsg = "dummyErrMsg";
      when(streamBridge.send(eq(topicName), any())).thenThrow(new RuntimeException(errMsg));

      final var file = new MockMultipartFile("file", new byte[]{});
      final var ex = assertThrows(RuntimeException.class, () -> underTest.uploadImage(file));

      assertEquals(errMsg, ex.getMessage());
      verify(streamBridge).send(eq(topicName), any());
      verifyNoMoreInteractions(streamBridge);
    }
  }

}
