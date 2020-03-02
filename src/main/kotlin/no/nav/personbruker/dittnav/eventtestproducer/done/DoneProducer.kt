package no.nav.personbruker.dittnav.eventtestproducer.done

import no.nav.brukernotifikasjon.schemas.Done
import no.nav.brukernotifikasjon.schemas.Nokkel
import no.nav.personbruker.dittnav.eventtestproducer.common.InnloggetBruker
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

    private val env = Environment()

    fun produceDoneEventForSpecifiedEvent(innloggetBruker: InnloggetBruker, eventThatsDone: Brukernotifikasjon) {
        val doneEvent = createDoneEvent(innloggetBruker)
        produceDoneEvent(doneEvent, createKeyForEvent(eventThatsDone.eventId, env.systemUserName))
        log.info("Har produsert et done-event for identen: ${innloggetBruker.getIdent()} sitt event med eventId: ${eventThatsDone.eventId}")
    }

    fun produceDoneEventForSuppliedEventId(innloggetBruker: InnloggetBruker, eventId: String) {
        val key = createKeyForEvent(eventId, env.systemUserName)
        val doneEvent = createDoneEvent(innloggetBruker)
        produceDoneEvent(doneEvent, key)
        log.info("Har produsert et done-event for identen: ${innloggetBruker.getIdent()} sitt event med eventId: $eventId")
    }

    private fun produceDoneEvent(doneEvent: Done, key: Nokkel) {
        KafkaProducer<Nokkel, Done>(Kafka.producerProps(env)).use { producer ->
            producer.send(ProducerRecord(doneTopicName, key, doneEvent))
        }
    }

    private fun createDoneEvent(innloggetBruker: InnloggetBruker): Done {
        val nowInMs = Instant.now().toEpochMilli()
        val build = Done.newBuilder()
                .setFodselsnummer(innloggetBruker.getIdent())
                .setTidspunkt(nowInMs)
                .setGrupperingsId("100$nowInMs")
        return build.build()
    }


}
