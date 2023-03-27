# paw-arbeidssoker-profilering

Profilering av arbeidssøker for å fatte et 14a-vedtak

## Kafka

### Consumer

```
docker exec -it arbeidssoker-profilering_kafka_1 kafka-console-consumer.sh --bootstrap-server 127.0.0.1:9092 --topic test
```

### Producer

```
docker exec -it arbeidssoker-profilering_kafka_1 kafka-console-producer.sh --broker-list 127.0.0.1:9092 --topic test
```
