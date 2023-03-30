package no.nav.paw.config

import io.github.cdimascio.dotenv.dotenv

val dotenv = dotenv { ignoreIfMissing = true }

data class Config(
    val database: DatabaseConfig = DatabaseConfig(
        dotenv["NAIS_DATABASE_PAW_ARBEIDSSOKER_PROFILERING_PROFILERING_HOST"],
        dotenv["NAIS_DATABASE_PAW_ARBEIDSSOKER_PROFILERING_PROFILERING_PORT"],
        dotenv["NAIS_DATABASE_PAW_ARBEIDSSOKER_PROFILERING_PROFILERING_NAME"],
        dotenv["NAIS_DATABASE_PAW_ARBEIDSSOKER_PROFILERING_PROFILERING_USER"],
        dotenv["NAIS_DATABASE_PAW_ARBEIDSSOKER_PROFILERING_PROFILERING_PASSWORD"],
        ),
    val naisEnv: NaisEnv = NaisEnv.current(),
    val unleashClientConfig: UnleashClientConfig = UnleashClientConfig(
        dotenv["UNLEASH_URL"],
        dotenv["NAIS_APP_NAME"]
    ),
    val kafka: KafkaConfig = KafkaConfig(
        dotenv["KAFKA_BROKER_URL"],
        dotenv["KAFKA_PRODUCER_ID"],
        dotenv["KAFKA_CONSUMER_GROUP_ID"],
        KafkaProducers(
            KafkaProducer(
                dotenv["KAFKA_PRODUCER_ARBEIDSSOKER_ENDRINGER_TOPIC"]
            )
        ),
        KafkaConsumers(
            KafkaConsumer(
                dotenv["KAFKA_CONSUMER_ARBEIDSSOKER_REGISTERING_TOPIC"]
            )
        )
    ),
    val aaregClientConfig: ServiceClientConfig = ServiceClientConfig(
        dotenv["AAREG_URL"],
        dotenv["AAREG_SCOPE"]
    )
)

data class DatabaseConfig(
    val host: String,
    val port: String,
    val name: String,
    val user: String,
    val password: String,
) {
    val jdbcUrl: String get() = "jdbc:postgresql://$host/$name?user=$user&password=$password"
}

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

data class UnleashClientConfig(
    val url: String,
    val appName: String
)

enum class NaisEnv(val clusterName: String) {
    Local("local"),
    DevGCP("dev-gcp"),
    ProdGCP("prod-gcp");

    companion object {
        fun current(): NaisEnv = when (System.getenv("NAIS_CLUSTER_NAME")) {
            DevGCP.clusterName -> DevGCP
            ProdGCP.clusterName -> ProdGCP
            else -> Local
        }
    }

    fun isLocal(): Boolean = this === Local
    fun isDevGCP(): Boolean = this === DevGCP
    fun isProdGCP(): Boolean = this === ProdGCP
}
