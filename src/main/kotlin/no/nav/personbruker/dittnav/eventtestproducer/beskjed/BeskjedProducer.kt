package no.nav.personbruker.dittnav.eventtestproducer.beskjed

import no.nav.brukernotifikasjon.schemas.Beskjed
import no.nav.brukernotifikasjon.schemas.Nokkel
import no.nav.personbruker.dittnav.eventtestproducer.common.InnloggetBruker
import no.nav.personbruker.dittnav.eventtestproducer.common.createKeyForEvent
import no.nav.personbruker.dittnav.eventtestproducer.common.kafka.KafkaProducerWrapper
import org.slf4j.LoggerFactory
import java.time.Instant
import java.time.temporal.ChronoUnit

class BeskjedProducer(private val beskjedKafkaProducer: KafkaProducerWrapper<Beskjed>, private val systembruker: String) {

    private val log = LoggerFactory.getLogger(BeskjedProducer::class.java)

    fun produceBeskjedEventForIdent(innloggetBruker: InnloggetBruker, dto: ProduceBeskjedDto) {
        try {
            val key = createKeyForEvent(systembruker)
            val event = createBeskjedForIdent(innloggetBruker, dto)
            sendEventToKafka(key, event)
        } catch (e: Exception) {
            log.error("Det skjedde en feil ved produsering av et event for brukeren $innloggetBruker", e)
        }
    }

    fun sendEventToKafka(key: Nokkel, event: Beskjed) {
        beskjedKafkaProducer.sendEvent(key, event)
    }

    fun createBeskjedForIdent(innloggetBruker: InnloggetBruker, dto: ProduceBeskjedDto): Beskjed {
        val nowInMs = Instant.now().toEpochMilli()
        val weekFromNowInMs = Instant.now().plus(7, ChronoUnit.DAYS).toEpochMilli()
        val build = Beskjed.newBuilder()
                .setFodselsnummer(innloggetBruker.ident)
                .setGrupperingsId("100$nowInMs")
                .setLink(dto.link)
                .setTekst(dto.tekst)
                .setTidspunkt(nowInMs)
                .setSynligFremTil(weekFromNowInMs)
                .setSikkerhetsnivaa(innloggetBruker.innloggingsnivaa)
                .setEksternVarsling(dto.eksternVarsling)
        return build.build()
    }

}
