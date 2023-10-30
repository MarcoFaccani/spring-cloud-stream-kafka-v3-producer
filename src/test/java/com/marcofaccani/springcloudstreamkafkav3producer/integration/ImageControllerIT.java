package com.marcofaccani.springcloudstreamkafkav3producer.integration;

import java.util.HashMap;

import com.marcofaccani.springcloudstreamkafkav3producer.constant.AppBindingChannels;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@EmbeddedKafka(topics = {"image-topic"}, partitions = 1)
@TestPropertySource(properties = {
    "spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}",
    "spring.kafka.admin.properties.bootstrap.servers=${spring.embedded.kafka.brokers}"} )
public class ImageControllerIT {

  String baseUrl;

  @LocalServerPort
  int appPort;

  @Autowired
  private WebTestClient webTestClient;

  @Autowired
  private EmbeddedKafkaBroker embeddedKafkaBroker;

  private Consumer<Integer, byte[]> consumer;


  @BeforeEach
  void setup() {
    baseUrl = "http://localhost:" + appPort;

    // Kafka Consumer Setup
    final var configs = new HashMap<>(KafkaTestUtils.consumerProps("dummy-group", "true", embeddedKafkaBroker));
    consumer = new DefaultKafkaConsumerFactory<>(configs, new IntegerDeserializer(), new ByteArrayDeserializer()).createConsumer();
    embeddedKafkaBroker.consumeFromAllEmbeddedTopics(consumer);
  }

  @AfterEach
  void tearDown() {
    consumer.close();
  }

  @Nested
  class UploadImage {

    @Test
    @SneakyThrows
    void shouldReturn200AndProduceMessageToKafka() {
      webTestClient.post()
          .uri(baseUrl + "/image")
          .contentType(MediaType.MULTIPART_FORM_DATA)
          .bodyValue(createMultipartBuilder("fileName", getImageBytesArray()).build())
          .exchange()
          .expectStatus().isOk();

      // consume message from topic
      final var consumedEvent = KafkaTestUtils.getSingleRecord(consumer, AppBindingChannels.IMAGE_TOPIC.getValue());

      // verify the message received by the consumer is as expected
      assertArrayEquals(getImageBytesArray(), consumedEvent.value());
    }
  }


  //--- Utility methods
  private MultipartBodyBuilder createMultipartBuilder(final String fileName, final byte[] content) {
    final var newMultipartBuilder = new MultipartBodyBuilder();
    newMultipartBuilder
        .part("file", content)
        .header(HttpHeaders.CONTENT_DISPOSITION, "form-data; name=file; filename=".concat(fileName));
    return newMultipartBuilder;
  }

  @SneakyThrows
  private byte[] getImageBytesArray() {
    return IOUtils.toByteArray(
        getClass().
            getClassLoader()
            .getResourceAsStream("static/image.png"));
  }

}
