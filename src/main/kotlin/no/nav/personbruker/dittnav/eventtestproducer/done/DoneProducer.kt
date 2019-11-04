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

    fun produceDoneEventForIdent(ident: String, eventThatsDone: Brukernotifikasjon) {
        KafkaProducer<String, Done>(Kafka.producerProps(Environment())).use { producer ->
            producer.send(ProducerRecord(doneTopicName, createDoneForIdent(ident, eventThatsDone)))
        }
        log.info("Har produsert et done-event for identen: $ident")
    }

    private fun createDoneForIdent(ident: String, eventThatsDone: Brukernotifikasjon): Done {
        val nowInMs = Instant.now().toEpochMilli()
        val build = Done.newBuilder()
                .setAktorId(ident)
                .setEventId(eventThatsDone.eventId)
                .setProdusent("DittNAV")
                .setTidspunkt(nowInMs)
                .setDokumentId("100$nowInMs")
        return build.build()
    }

}
