package no.nav.personbruker.dittnav.eventtestproducer.common.kafka

import no.nav.brukernotifikasjon.schemas.Nokkel
import org.apache.kafka.clients.producer.Callback
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.LoggerFactory

class KafkaProducerWrapper<T>(
        val topicName: String,
        val kafkaProducer: KafkaProducer<Nokkel, T>) {

    private val log = LoggerFactory.getLogger(KafkaProducerWrapper::class.java)

    fun sendEvent(key: Nokkel, event: T) {
        val producerRecord = ProducerRecord(topicName, key, event)
        kafkaProducer.send(producerRecord, Callback { metadata, exception ->
            if (exception != null) {
                throw exception
            }
        })
    }

    fun flushAndClose() {
        try {
            kafkaProducer.flush()
            kafkaProducer.close()
            log.info("Produsent for kafka-event er flushet og lukket. Topic: $topicName")
        } catch (e: Exception) {
            log.warn("Klarte ikke å flushe og lukke produsent. Det kan være eventer som ikke ble produsert. Topic: $topicName")
        }
    }
}