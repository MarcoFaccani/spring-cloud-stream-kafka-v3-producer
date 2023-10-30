package com.marcofaccani.springcloudstreamkafkav3producer.constant;

public enum AppBindingChannels {

  IMAGE_TOPIC("image-topic");

  private final String value;

  AppBindingChannels(final String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }

}
