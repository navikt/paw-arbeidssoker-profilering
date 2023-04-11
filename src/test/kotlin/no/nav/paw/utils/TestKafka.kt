package no.nav.paw.utils

import org.testcontainers.containers.KafkaContainer
import org.testcontainers.utility.DockerImageName

object TestKafka {
    private val kafkaContainer = KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.0"))

    fun setup(): KafkaContainer {
        if (!kafkaContainer.isRunning) {
            kafkaContainer.start()
        }
        return kafkaContainer
    }
}
