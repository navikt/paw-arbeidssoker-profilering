package no.nav.paw.kafka.producers

import com.fasterxml.jackson.databind.ObjectMapper
import no.nav.common.kafka.producer.KafkaProducerClient
import no.nav.paw.config.Config
import no.nav.paw.domain.ProfileringEndringMelding
import org.apache.kafka.clients.producer.ProducerRecord
import java.util.UUID

class ProfileringEndringProducer(
    private val kafkaProducerClient: KafkaProducerClient<String, String>,
    private val config: Config,
    private val objectMapper: ObjectMapper
) {
    fun publish(value: ProfileringEndringMelding) {
        val record: ProducerRecord<String, String> = ProducerRecord(
            config.kafka.producers.arbeidssokerEndringer.topic,
            UUID.randomUUID().toString(),
            objectMapper.writeValueAsString(value)
        )
        kafkaProducerClient.sendSync(record)
    }
}
