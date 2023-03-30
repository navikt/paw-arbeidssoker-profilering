# paw-arbeidssoker-profilering

Profilering av arbeidssøker for å fatte et 14a-vedtak

## Lokalt oppsett

```sh
cp .env-example .env
```

```sh
docker-compose up -d
```

## Kafka

### Producer

```sh
# Eksempel melding
cat src/main/resources/arbeidssoker-registrert-kafka-melding.json | jq -c .
docker exec -it arbeidssoker-profilering_kafka_1 kafka-console-producer.sh --broker-list 127.0.0.1:9092 --topic arbeidssoker-registrering-v2
```

### Consumer

```sh
docker exec -it arbeidssoker-profilering_kafka_1 kafka-console-consumer.sh --bootstrap-server 127.0.0.1:9092 --topic arbeidssoker-profilering-endring-v1
```
