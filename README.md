# paw-arbeidssoker-profilering

## Kafka

### Consumer

```
docker exec -it arbeidssoker-profilering_kafka_1 kafka-console-consumer.sh --bootstrap-server 127.0.0.1:9092 --topic test
```

### Producer

```
docker exec -it arbeidssoker-profilering_kafka_1 kafka-console-producer.sh --broker-list 127.0.0.1:9092 --topic test
```
