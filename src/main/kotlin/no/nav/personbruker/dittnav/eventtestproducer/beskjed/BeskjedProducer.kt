package no.nav.personbruker.dittnav.eventtestproducer.beskjed

import kotlinx.datetime.toJavaLocalDateTime
import no.nav.brukernotifikasjon.schemas.Beskjed
import no.nav.brukernotifikasjon.schemas.Nokkel
import no.nav.brukernotifikasjon.schemas.builders.legacy.BeskjedBuilder
import no.nav.personbruker.dittnav.eventtestproducer.common.InnloggetBruker
import no.nav.personbruker.dittnav.eventtestproducer.common.createKeyForEvent
import no.nav.personbruker.dittnav.eventtestproducer.common.getPrefererteKanaler
import no.nav.personbruker.dittnav.eventtestproducer.common.kafka.KafkaProducerWrapper
import org.slf4j.LoggerFactory
import java.net.URL
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit

class BeskjedProducer(private val beskjedKafkaProducer: KafkaProducerWrapper<Nokkel, Beskjed>, private val systembruker: String) {

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
        val now = LocalDateTime.now(ZoneOffset.UTC)
        val builder = BeskjedBuilder()
                .withFodselsnummer(innloggetBruker.ident)
                .withGrupperingsId(dto.grupperingsid)
                .withTekst(dto.tekst)
                .withTidspunkt(now)
                .withSynligFremTil(dto.synligFremTil?.toJavaLocalDateTime())
                .withSikkerhetsnivaa(innloggetBruker.innloggingsnivaa)
                .withEksternVarsling(dto.eksternVarsling)
                .withEpostVarslingstekst(dto.epostVarslingstekst)
                .withEpostVarslingstittel(dto.epostVarslingstittel)
                .withSmsVarslingstekst(dto.smsVarslingstekst)
                .withPrefererteKanaler(*getPrefererteKanaler(dto.prefererteKanaler).toTypedArray())
        if(!dto.link.isNullOrBlank()) {
            builder.withLink(URL(dto.link))
        }
        return builder.build()
    }
}
