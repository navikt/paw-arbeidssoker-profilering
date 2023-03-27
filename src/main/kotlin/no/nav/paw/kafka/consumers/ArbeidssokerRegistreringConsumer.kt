package no.nav.paw.kafka.consumers

import com.fasterxml.jackson.databind.ObjectMapper
import no.nav.paw.domain.ArbeidssokerRegistrert
import no.nav.paw.services.ProfileringService
import no.nav.paw.utils.logger
import org.apache.kafka.clients.consumer.KafkaConsumer
import java.time.Duration

class ArbeidssokerRegistreringConsumer(
    private val consumer: KafkaConsumer<String, String>,
    private val profileringService: ProfileringService,
    private val jsonObjectMapper: ObjectMapper
) {
    init {
        logger.info("Lytter på topic")
        consumer.subscribe(listOf("test"))
    }

    fun start() {
        while (true) {
            val records = consumer.poll(Duration.ofMillis(300))
            for (record in records) {
                val arbeidssokerRegistrertMelding = jsonObjectMapper.readValue(record.value(), ArbeidssokerRegistrert::class.java)
                profileringService.opprettProfilering(arbeidssokerRegistrertMelding)

                logger.info("Mottok melding fra topic: ${record.value()}")
            }
            consumer.commitAsync()
        }
    }
}
