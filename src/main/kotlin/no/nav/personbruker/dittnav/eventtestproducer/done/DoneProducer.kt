package no.nav.personbruker.dittnav.eventtestproducer.done

import no.nav.brukernotifikasjon.schemas.Done
import no.nav.brukernotifikasjon.schemas.Nokkel
import no.nav.brukernotifikasjon.schemas.builders.legacy.DoneBuilder
import no.nav.personbruker.dittnav.eventtestproducer.common.kafka.KafkaProducerWrapper
import no.nav.personbruker.dittnav.eventtestproducer.common.InnloggetBruker
import no.nav.personbruker.dittnav.eventtestproducer.common.createKeyForEvent
import no.nav.personbruker.dittnav.eventtestproducer.common.database.Brukernotifikasjon
import org.slf4j.LoggerFactory
import java.time.LocalDateTime
import java.time.ZoneOffset

class DoneProducer(private val doneKafkaProducer: KafkaProducerWrapper<Nokkel, Done>, private val systembruker: String) {

    private val log = LoggerFactory.getLogger(DoneProducer::class.java)

    fun produceDoneEventForSpecifiedEvent(innloggetBruker: InnloggetBruker, eventThatsDone: Brukernotifikasjon) {
        try {
            val doneEvent = createDoneEvent(innloggetBruker)
            val key = createKeyForEvent(eventThatsDone.eventId, systembruker)
            sendEventToKafka(key, doneEvent)
        } catch (e: Exception) {
            log.error("Det skjedde en feil ved produsering av et event for brukeren $innloggetBruker", e)
        }
    }

    fun sendEventToKafka(key: Nokkel, event: Done) {
        doneKafkaProducer.sendEvent(key, event)
    }

    fun createDoneEvent(innloggetBruker: InnloggetBruker): Done {
        val now = LocalDateTime.now(ZoneOffset.UTC)
        val builder = DoneBuilder()
                .withFodselsnummer(innloggetBruker.ident)
                .withTidspunkt(now)
                .withGrupperingsId("100$now")
        return builder.build()
    }

}
