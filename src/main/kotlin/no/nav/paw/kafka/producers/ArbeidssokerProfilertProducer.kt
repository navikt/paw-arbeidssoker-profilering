package no.nav.paw.kafka.producers

import no.nav.common.kafka.producer.KafkaProducerClient
import no.nav.paw.profilering.ArbeidssokerProfilertEvent
import no.nav.paw.utils.CallId.callId
import no.nav.paw.utils.logger
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.header.internals.RecordHeader
import java.nio.charset.StandardCharsets
import java.util.*

class ArbeidssokerProfilertProducer(
    private val kafkaProducerClient: KafkaProducerClient<String, ArbeidssokerProfilertEvent>,
    private val topic: String
) {
    fun publish(value: ArbeidssokerProfilertEvent) {
        val record: ProducerRecord<String, ArbeidssokerProfilertEvent> = ProducerRecord(
            topic,
            null,
            UUID.randomUUID().toString(),
            value,
            listOf(RecordHeader("CallId", callId.toByteArray(StandardCharsets.UTF_8)))
        )

        kafkaProducerClient.sendSync(record)
        logger.info("Sendte melding om fullført profilering til $topic")
    }
}
