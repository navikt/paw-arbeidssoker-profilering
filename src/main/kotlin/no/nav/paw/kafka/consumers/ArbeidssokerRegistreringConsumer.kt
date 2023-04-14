package no.nav.paw.kafka.consumers

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import no.nav.common.featuretoggle.UnleashClient
import no.nav.paw.domain.ArbeidssokerRegistrert
import no.nav.paw.services.ProfileringService
import no.nav.paw.utils.CallId.leggTilCallId
import no.nav.paw.utils.logger
import org.apache.kafka.clients.consumer.KafkaConsumer
import java.time.Duration

class ArbeidssokerRegistreringConsumer(
    private val topic: String,
    private val consumer: KafkaConsumer<String, String>,
    private val profileringService: ProfileringService,
    private val unleashClient: UnleashClient,
    private val objectMapper: ObjectMapper
) {

    fun start() {
        logger.info("Lytter på topic $topic")
        consumer.subscribe(listOf(topic))

        val konsumerArbeidssokerRegistrert =
            unleashClient.isEnabled("paw-arbeidssoker-profilering.consumer-arbeidssoker-registert")
        if (!konsumerArbeidssokerRegistrert) {
            logger.info("Konsumering av $topic er deaktivert")
        }

        while (true) {
            consumer.poll(Duration.ofMillis(500)).forEach { post ->
                try {
                    leggTilCallId()
                    val arbeidssokerRegistrert: ArbeidssokerRegistrert = objectMapper.readValue(post.value())
                    profileringService.opprettProfilering(arbeidssokerRegistrert)

                    logger.info("Mottok melding fra $topic med offset ${post.offset()}p${post.partition()}")

                    consumer.commitSync()
                } catch (error: Exception) {
                    logger.error("Feil ved konsumering av melding fra $topic: ${error.message}")
                    throw error
                }
            }
        }
    }
}
