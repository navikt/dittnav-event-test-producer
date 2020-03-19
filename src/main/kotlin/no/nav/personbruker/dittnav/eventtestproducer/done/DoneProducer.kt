package no.nav.personbruker.dittnav.eventtestproducer.done

import no.nav.brukernotifikasjon.schemas.Done
import no.nav.brukernotifikasjon.schemas.Nokkel
import no.nav.personbruker.dittnav.eventtestproducer.common.InnloggetBruker
import no.nav.personbruker.dittnav.eventtestproducer.common.createKeyForEvent
import no.nav.personbruker.dittnav.eventtestproducer.common.database.Brukernotifikasjon
import no.nav.personbruker.dittnav.eventtestproducer.config.Environment
import no.nav.personbruker.dittnav.eventtestproducer.config.EventType
import no.nav.personbruker.dittnav.eventtestproducer.config.Kafka
import no.nav.personbruker.dittnav.eventtestproducer.config.Kafka.doneTopicName
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.LoggerFactory
import java.time.Instant

class DoneProducer(private val env: Environment) {

    private val log = LoggerFactory.getLogger(DoneProducer::class.java)
    private val kafkaProducer = KafkaProducer<Nokkel, Done>(Kafka.producerProps(env, EventType.DONE))

    fun produceDoneEventForSpecifiedEvent(innloggetBruker: InnloggetBruker, eventThatsDone: Brukernotifikasjon) {
        val doneEvent = createDoneEvent(innloggetBruker)
        val key = createKeyForEvent(eventThatsDone.eventId, env.systemUserName)

        try {
            kafkaProducer.send(ProducerRecord(doneTopicName, key, doneEvent))
            log.info("Har produsert et done-event for eventet med nøkkel $key")

        } catch (e: Exception) {
            log.error("Det skjedde en feil ved produsering av et event for brukeren $innloggetBruker", e)
        }
    }

    fun close() {
        try {
            kafkaProducer.close()
            log.info("Produsenten er lukket.")

        } catch (e: Exception) {
            log.warn("Klarte ikke å lukke produsenten. Det kan være venter som ikke ble produsert.")
        }
    }

    private fun createDoneEvent(innloggetBruker: InnloggetBruker): Done {
        val nowInMs = Instant.now().toEpochMilli()
        val build = Done.newBuilder()
                .setFodselsnummer(innloggetBruker.ident)
                .setTidspunkt(nowInMs)
                .setGrupperingsId("100$nowInMs")
        return build.build()
    }

}
