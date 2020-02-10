package no.nav.personbruker.dittnav.eventtestproducer.done

import no.nav.brukernotifikasjon.schemas.Done
import no.nav.brukernotifikasjon.schemas.Nokkel
import no.nav.personbruker.dittnav.eventtestproducer.common.createKeyForEvent
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
        val doneEvent = createDoneEvent(ident)
        produceDoneEvent(doneEvent, createKeyForEvent(eventThatsDone.eventId))
        log.info("Har produsert et done-event for identen: $ident sitt event med eventId: ${eventThatsDone.eventId}")
    }

    fun produceDoneEventForSuppliedEventId(ident: String, eventId: String) {
        val doneEvent = createDoneEvent(ident)
        produceDoneEvent(doneEvent, createKeyForEvent(eventId))
        log.info("Har produsert et done-event for identen: $ident sitt event med eventId: $eventId")
    }

    private fun produceDoneEvent(doneEvent : Done, key: Nokkel) {
        KafkaProducer<Nokkel, Done>(Kafka.producerProps(Environment())).use { producer ->
            producer.send(ProducerRecord(doneTopicName, key, doneEvent))
        }
    }

    private fun createDoneEvent(ident: String): Done {
        val nowInMs = Instant.now().toEpochMilli()
        val build = Done.newBuilder()
                .setFodselsnummer(ident)
                .setTidspunkt(nowInMs)
                .setGrupperingsId("100$nowInMs")
        return build.build()
    }


}
