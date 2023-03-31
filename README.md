# paw-arbeidssoker-profilering

Profilering av arbeidssøker for å fatte et 14a-vedtak

## Dokumentasjon

https://arbeidssoker-profilering.intern.dev.nav.no/docs

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
docker exec -it paw-arbeidssoker-profilering_kafka_1 kafka-console-producer.sh --broker-list 127.0.0.1:9092 --topic arbeidssoker-registrering-v2
```

### Consumer

```sh
docker exec -it paw-arbeidssoker-profilering_kafka_1 kafka-console-consumer.sh --bootstrap-server 127.0.0.1:9092 --topic arbeidssoker-profilering-endring-v1
```

### Autentisering

For å kalle APIet lokalt må man være autentisert med et Bearer token.

Vi benytter mock-ouath2-server til å utstede tokens på lokal maskin. Følgende steg kan benyttes til å generere opp et token:

1. Sørg for at containeren for mock-oauth2-server kjører lokalt (docker-compose up -d)
2. Naviger til [mock-oauth2-server sin side for debugging av tokens](http://localhost:8081/default/debugger)
3. Generer et token
4. Trykk på knappen Get a token
5. Skriv inn et random username og NAVident i optional claims, f.eks.

```json
{ "acr": "Level3", "pid": "26118611111" }
```

6. Trykk Sign in
7. Kopier verdien for access_token og benytt denne som Bearer i Authorization-header

8. Eksempel:

$ curl localhost:8080/api/v1/innsatsgrupper -H 'Authorization: Bearer <access_token>'

