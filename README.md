## Overview
This project aims to showcase how to integrate with a Kafka Cluster using Spring Cloud Stream. It exposes a POST endpoint that allows a client to upload a file, which is then sent to a Kafka topic and consumed by a [listener](https://github.com/MarcoFaccani/spring-cloud-stream-kafka-v3-consumer)
> Please note the focus of this project is not the REST API nor the efficiency of it.

## Tech Stack
- Spring Boot V2.7
- Java 11
- Spring Cloud Stream Kafka Binder V3

## Run
1. Spin locally a Kafka Broker on port `9092`
2. Run the producer app and the [consumer app](https://github.com/MarcoFaccani/spring-cloud-stream-kafka-v3-consumer)
3. Using your favourite REST Client, do `POST http://localhost:8080/image` and pass a file.
4. Go check the consumer's folder `/resources/files`, you should find the file saved there.