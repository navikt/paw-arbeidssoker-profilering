package no.nav.paw.kafka.producers

import com.fasterxml.jackson.databind.ObjectMapper
import no.nav.common.kafka.producer.KafkaProducerClient
import no.nav.paw.config.Config
import no.nav.paw.domain.ProfileringEndringMelding
import no.nav.paw.utils.logger
import org.apache.kafka.clients.producer.ProducerRecord
import java.util.UUID

class ProfileringEndringProducer(
    private val kafkaProducerClient: KafkaProducerClient<String, String>,
    private val topic: String,
    private val objectMapper: ObjectMapper
) {
    fun publish(value: ProfileringEndringMelding) {
        val record: ProducerRecord<String, String> = ProducerRecord(
            topic,
            UUID.randomUUID().toString(),
            objectMapper.writeValueAsString(value)
        )
        kafkaProducerClient.sendSync(record)
        logger.info("Sendte melding om fullført profilering til $topic")
    }
}
