package no.nav.paw.kafka.consumers

import no.nav.paw.utils.logger
import org.apache.kafka.clients.consumer.KafkaConsumer
import java.time.Duration

class ArbeidssokerRegistreringConsumer(private val consumer: KafkaConsumer<String, String>) {
    init {
        logger.info("Lytter på topic")
        consumer.subscribe(listOf("test"))
    }

    fun start() {
        while (true) {
            val records = consumer.poll(Duration.ofMillis(300))
            for (record in records) {
                logger.info("Mottok melding fra topic: ${record.value()}")
            }
            consumer.commitAsync()
        }
    }
}
