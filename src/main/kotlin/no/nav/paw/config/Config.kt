package no.nav.paw.config

import io.getunleash.util.UnleashConfig
import io.github.cdimascio.dotenv.dotenv
import no.nav.security.token.support.v2.RequiredClaims

val dotenv = dotenv { ignoreIfMissing = true }

data class Config(
    val database: DatabaseConfig = DatabaseConfig(
        dotenv["NAIS_DATABASE_PAW_ARBEIDSSOKER_PROFILERING_PROFILERING_HOST"],
        dotenv["NAIS_DATABASE_PAW_ARBEIDSSOKER_PROFILERING_PROFILERING_PORT"],
        dotenv["NAIS_DATABASE_PAW_ARBEIDSSOKER_PROFILERING_PROFILERING_DATABASE"],
        dotenv["NAIS_DATABASE_PAW_ARBEIDSSOKER_PROFILERING_PROFILERING_USERNAME"],
        dotenv["NAIS_DATABASE_PAW_ARBEIDSSOKER_PROFILERING_PROFILERING_PASSWORD"]
    ),
    val naisEnv: NaisEnv = NaisEnv.current(),
    val unleashClientConfig: UnleashConfig = UnleashConfig.builder()
        .appName(dotenv["NAIS_APP_NAME"])
        .instanceId(dotenv["NAIS_APP_NAME"])
        .unleashAPI(dotenv["UNLEASH_SERVER_API_URL"])
        .apiKey(dotenv["UNLEASH_SERVER_API_TOKEN"])
        .synchronousFetchOnInitialisation(true)
        .build(),
    val authentication: List<AuthProvider> = listOf(
        AuthProvider(
            name = "tokenx",
            discoveryUrl = dotenv["TOKEN_X_WELL_KNOWN_URL"],
            acceptedAudience = listOf(dotenv["TOKEN_X_CLIENT_ID"]),
            requiredClaims = RequiredClaims("tokenx", arrayOf("pid"))
        )
    ),
    val kafka: KafkaConfig = KafkaConfig(
        dotenv["KAFKA_BROKER_URL"],
        dotenv["KAFKA_PRODUCER_ID"],
        dotenv["KAFKA_CONSUMER_GROUP_ID"],
        dotenv["KAFKA_SCHEMA_REGISTRY"],
        "${dotenv["KAFKA_SCHEMA_REGISTRY_USER"]}:${dotenv["KAFKA_SCHEMA_REGISTRY_PASSWORD"]}",
        KafkaProducers(
            KafkaProducer(
                dotenv["KAFKA_PRODUCER_ARBEIDSSOKER_PROFILERT_TOPIC"]
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
    val database: String,
    val username: String,
    val password: String
) {
    val jdbcUrl: String get() = "jdbc:postgresql://$host:$port/$database?user=$username&password=$password"
}

data class KafkaConfig(
    val brokerUrl: String? = null,
    val producerId: String,
    val consumerGroupId: String,
    val schemaRegisteryUrl: String?,
    val schemaRegisteryUserInfo: String?,
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

data class AuthProvider(
    val name: String,
    val discoveryUrl: String,
    val acceptedAudience: List<String>,
    val cookieName: String? = null,
    val requiredClaims: RequiredClaims? = null
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
