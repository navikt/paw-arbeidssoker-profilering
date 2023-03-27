package no.nav.paw.config

data class Config(
    val database: DatabaseConfig = DatabaseConfig(
        getEnvVar("NAIS_DATABASE_PAW_ARBEIDSSOKER_PROFILERING_ARBEIDSSOKER_PROFILERING_URL")
    ),
    val kafka: KafkaConfig = KafkaConfig(
        getEnvVar("KAFKA_BROKER_URL"),
        getEnvVar("KAFKA_PRODUCER_ID"),
        getEnvVar("KAFKA_CONSUMER_GROUP_ID"),
        KafkaProducers(
            KafkaProducer(
                getEnvVar("KAFKA_PRODUCER_ARBEIDSSOKER_ENDRINGER_TOPIC")
            )
        ),
        KafkaConsumers(
            KafkaConsumer(
                getEnvVar("KAFKA_CONSUMER_ARBEIDSSOKER_REGISTERING_TOPIC")
            )
        )
    ),
    val aaregClientConfig: ServiceClientConfig = ServiceClientConfig(
        getEnvVar("AAREG_URL"),
        getEnvVar("AAREG_SCOPE")
    )
)

data class DatabaseConfig(
    val url: String
)

data class KafkaConfig(
    val brokerUrl: String? = null,
    val producerId: String,
    val consumerGroupId: String,
    val producers: KafkaProducers,
    val consumers: KafkaConsumers
)

data class KafkaProducers(
    val arbeidssokerEndringer: KafkaProducer
)

data class KafkaConsumers(
    val arbeidssokerRegistrering: KafkaConsumer
)

data class KafkaConsumer(
    val topic: String
)

data class KafkaProducer(
    val topic: String
)

data class ServiceClientConfig(
    val url: String,
    val scope: String
)

fun getEnvVar(varName: String, defaultValue: String? = null) =
    System.getenv(varName) ?: defaultValue
        ?: throw RuntimeException("Environment: Missing required variable \"$varName\"")
