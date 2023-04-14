package no.nav.paw.kafka.producers

import com.fasterxml.jackson.databind.ObjectMapper
import no.nav.common.kafka.producer.KafkaProducerClient
import no.nav.paw.domain.dto.ProfileringEndringMeldingDto
import no.nav.paw.utils.CallId.callId
import no.nav.paw.utils.logger
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.header.internals.RecordHeader
import java.nio.charset.StandardCharsets
import java.util.*

class ProfileringEndringProducer(
    private val kafkaProducerClient: KafkaProducerClient<String, String>,
    private val topic: String,
    private val objectMapper: ObjectMapper
) {
    fun publish(value: ProfileringEndringMeldingDto) {
        val record: ProducerRecord<String, String> = ProducerRecord(
            topic,
            null,
            UUID.randomUUID().toString(),
            objectMapper.writeValueAsString(value),
            listOf(RecordHeader("CallId", callId.toByteArray(StandardCharsets.UTF_8)))
        )

        kafkaProducerClient.sendSync(record)
        logger.info("Sendte melding om fullført profilering til $topic")
    }
}
