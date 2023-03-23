package no.nav.paw.config

data class Config(
    val database: DatabaseConfig = DatabaseConfig(
        getEnvVar("NAIS_DATABASE_PAW_ARBEIDSSOKER_PROFILERING_ARBEIDSSOKER_PROFILERING_URL")
    ),
    val kafka: KafkaConfig = KafkaConfig(
        getEnvVar("KAFKA_BROKER_URL"),
        getEnvVar("KAFKA_PRODUCER_ID"),
        KafkaProducers(
            KafkaProducer(
                getEnvVar("KAFKA_PRODUCER_ARBEIDSSOKER_ENDRINGER_TOPIC")
            )
        )
    )
)
data class DatabaseConfig(
    val url: String
)
data class KafkaConfig(
    val brokerUrl: String? = null,
    val producerId: String,
    val producers: KafkaProducers
)
data class KafkaProducers(
    val arbeidssokerEndringer: KafkaProducer
)
data class KafkaProducer(
    val topic: String
)
fun getEnvVar(varName: String, defaultValue: String? = null) =
    System.getenv(varName) ?: defaultValue
        ?: throw RuntimeException("Environment: Missing required variable \"$varName\"")
