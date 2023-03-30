package no.nav.paw.plugins

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.jackson.jackson
import io.ktor.server.application.Application
import io.ktor.server.application.install
import no.nav.common.kafka.producer.util.KafkaProducerClientBuilder
import no.nav.common.kafka.util.KafkaPropertiesBuilder
import no.nav.paw.aareg.AaregClient
import no.nav.paw.auth.TokenService
import no.nav.paw.config.Config
import no.nav.paw.config.NaisEnv
import no.nav.paw.config.createDatabaseConfig
import no.nav.paw.kafka.consumers.ArbeidssokerRegistreringConsumer
import no.nav.paw.kafka.producers.ProfileringEndringProducer
import no.nav.paw.repository.ProfileringRepository
import no.nav.paw.services.ProfileringService
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

fun Application.configureDependencyInjection(config: Config) {
    install(Koin) {
        modules(
            module {
                single {
                    createDatabaseConfig(config.database.url)
                }

                single {
                    config
                }

                single {
                    HttpClient {
                        install(ContentNegotiation) {
                            jackson()
                        }
                    }
                }

                single {
                    jacksonObjectMapper().apply {
                        registerKotlinModule()
                        disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                        disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                        registerModule(JavaTimeModule())
                    }
                }

                single {
                    val producerProperties = KafkaPropertiesBuilder.producerBuilder()
                        .withBaseProperties()
                        .withProducerId(config.kafka.producerId)
                        .withBrokerUrl(config.kafka.brokerUrl)
                        .withSerializers(StringSerializer::class.java, StringSerializer::class.java)
                        .build()

                    val client = KafkaProducerClientBuilder.builder<String, String>()
                        .withProperties(producerProperties)
                        .build()
                    client
                }

                single {
                    val consumerProperties = KafkaPropertiesBuilder.consumerBuilder()
                        .withBaseProperties()
                        .withConsumerGroupId(config.kafka.consumerGroupId)
                        .withBrokerUrl(config.kafka.brokerUrl)
                        .withDeserializers(StringDeserializer::class.java, StringDeserializer::class.java)
                        .build()

                    KafkaConsumer<String, String>(consumerProperties)
                }

                single {
                    AaregClient(config.aaregClientConfig.url) {
                        when (NaisEnv.current()) {
                            NaisEnv.Local -> "testToken"
                            else -> TokenService().createMachineToMachineToken(config.aaregClientConfig.scope)
                        }
                    }
                }

                single { ProfileringRepository(get()) }
                single { ProfileringEndringProducer(get(), get(), get()) }
                single { ProfileringService(get(), get(), get(), get()) }
                single {
                    ArbeidssokerRegistreringConsumer(
                        config.kafka.consumers.arbeidssokerRegistrering.topic,
                        get(),
                        get(),
                        get()
                    )
                }
            }
        )
    }
}
