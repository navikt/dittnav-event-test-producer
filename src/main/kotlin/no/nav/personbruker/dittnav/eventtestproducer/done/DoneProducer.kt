package no.nav.personbruker.dittnav.eventtestproducer.done

import no.nav.brukernotifikasjon.schemas.Done
import no.nav.personbruker.dittnav.eventtestproducer.common.database.Brukernotifikasjon
import no.nav.personbruker.dittnav.eventtestproducer.config.Environment
import no.nav.personbruker.dittnav.eventtestproducer.config.Kafka
import no.nav.personbruker.dittnav.eventtestproducer.config.Kafka.doneTopicName
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.LoggerFactory
import java.time.Instant

object DoneProducer {

    private val log = LoggerFactory.getLogger(DoneProducer::class.java)

    fun produceDoneEventForSpecifiedEvent(ident: String, eventThatsDone: Brukernotifikasjon) {
        val doneEvent = createDoneEvent(ident, eventThatsDone.eventId)
        produceDoneEvent(doneEvent)
        log.info("Har produsert et done-event for identen: $ident sitt event med eventId: ${eventThatsDone.eventId}")
    }

    fun produceDoneEventForSuppliedEventId(ident: String, eventId: String) {
        val doneEvent = createDoneEvent(ident, eventId)
        produceDoneEvent(doneEvent)
        log.info("Har produsert et done-event for identen: $ident sitt event med eventId: $eventId")
    }

    private fun produceDoneEvent(doneEvent : Done) {
        KafkaProducer<String, Done>(Kafka.producerProps(Environment())).use { producer ->
            producer.send(ProducerRecord(doneTopicName, doneEvent))
        }
    }

    private fun createDoneEvent(ident: String, eventId: String): Done {
        val nowInMs = Instant.now().toEpochMilli()
        val build = Done.newBuilder()
                .setFodselsnummer(ident)
                .setEventId(eventId)
                .setProdusent("DittNAV")
                .setTidspunkt(nowInMs)
                .setGrupperingsId("100$nowInMs")
        return build.build()
    }

}
