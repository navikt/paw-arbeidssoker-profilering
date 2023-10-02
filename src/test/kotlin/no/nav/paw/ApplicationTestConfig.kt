package no.nav.paw

import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import no.nav.paw.config.AuthProvider
import no.nav.paw.config.Config
import no.nav.paw.config.DatabaseConfig
import no.nav.paw.config.KafkaConfig
import no.nav.paw.config.KafkaConsumer
import no.nav.paw.config.KafkaConsumers
import no.nav.paw.config.KafkaProducer
import no.nav.paw.config.KafkaProducers
import no.nav.paw.config.dotenv
import no.nav.paw.plugins.configureAuthentication
import no.nav.paw.plugins.configureDependencyInjection
import no.nav.paw.plugins.configureHTTP
import no.nav.paw.plugins.configureLogging
import no.nav.paw.plugins.configureSerialization
import no.nav.security.mock.oauth2.MockOAuth2Server
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.containers.PostgreSQLContainer

fun <R> withTestApplication(
    config: Config,
    test: suspend ApplicationTestBuilder.() -> R
) {
    testApplication {
        application {
            configureDependencyInjection(config)
            configureAuthentication(config.authentication)
            configureHTTP()
            configureLogging()
            configureSerialization()
        }
        test()
    }
}

fun createConfig(
    oAuth2Server: MockOAuth2Server,
    postgreSQLContainer: PostgreSQLContainer<*>,
    kafkaContainer: KafkaContainer
): Config =
    Config(
        database = DatabaseConfig(
            postgreSQLContainer.host,
            postgreSQLContainer.firstMappedPort.toString(),
            "profilering",
            postgreSQLContainer.username,
            postgreSQLContainer.password
        ),
        kafka = KafkaConfig(
            kafkaContainer.bootstrapServers,
            "test-producer",
            "test-consumer",
            dotenv["KAFKA_SCHEMA_REGISTRY"],
            null,
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
        authentication = listOf(
            AuthProvider(
                "tokenx",
                oAuth2Server.wellKnownUrl("default").toString(),
                listOf("default")
            )
        )
    )
