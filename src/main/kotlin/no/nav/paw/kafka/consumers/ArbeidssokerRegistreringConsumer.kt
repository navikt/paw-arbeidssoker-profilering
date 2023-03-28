package no.nav.paw.kafka.consumers

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import no.nav.paw.domain.ArbeidssokerRegistrert
import no.nav.paw.services.ProfileringService
import no.nav.paw.utils.logger
import org.apache.kafka.clients.consumer.KafkaConsumer
import java.time.Duration

class ArbeidssokerRegistreringConsumer(
    private val topic: String,
    private val consumer: KafkaConsumer<String, String>,
    private val profileringService: ProfileringService,
    private val objectMapper: ObjectMapper
) {
    init {
        logger.info("Lytter på topic $topic")
        consumer.subscribe(listOf(topic))
    }

    fun start() {
        while (true) {
            val poster = consumer.poll(Duration.ofMillis(300))
            for (post in poster) {
                val arbeidssokerRegistrertMelding: ArbeidssokerRegistrert = objectMapper.readValue(post.value())
                profileringService.opprettProfilering(arbeidssokerRegistrertMelding)

                logger.info("Mottok melding fra $topic: ${post.value()}")
            }
            consumer.commitAsync()
        }
    }
}
