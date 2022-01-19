package no.nav.personbruker.dittnav.eventtestproducer.oppgave

import kotlinx.datetime.toJavaLocalDateTime
import no.nav.brukernotifikasjon.schemas.Nokkel
import no.nav.brukernotifikasjon.schemas.Oppgave
import no.nav.brukernotifikasjon.schemas.builders.legacy.OppgaveBuilder
import no.nav.personbruker.dittnav.eventtestproducer.common.InnloggetBruker
import no.nav.personbruker.dittnav.eventtestproducer.common.createKeyForEvent
import no.nav.personbruker.dittnav.eventtestproducer.common.getPrefererteKanaler
import no.nav.personbruker.dittnav.eventtestproducer.common.kafka.KafkaProducerWrapper
import org.slf4j.LoggerFactory
import java.net.URL
import java.time.LocalDateTime
import java.time.ZoneOffset

class OppgaveProducer(private val oppgaveKafkaProducer: KafkaProducerWrapper<Nokkel, Oppgave>, private val systembruker: String) {

    private val log = LoggerFactory.getLogger(OppgaveProducer::class.java)

    fun produceOppgaveEventForIdent(innloggetBruker: InnloggetBruker, dto: ProduceOppgaveDto) {
        try {
            val key = createKeyForEvent(systembruker)
            val event = createOppgaveForIdent(innloggetBruker, dto)
            sendEventToKafka(key, event)
        } catch (e: Exception) {
            log.error("Det skjedde en feil ved produsering av et event for brukeren $innloggetBruker", e)
        }
    }

    fun sendEventToKafka(key: Nokkel, event: Oppgave) {
        oppgaveKafkaProducer.sendEvent(key, event)
    }

    fun createOppgaveForIdent(innloggetBruker: InnloggetBruker, dto: ProduceOppgaveDto): Oppgave {
        val now = LocalDateTime.now(ZoneOffset.UTC)
        val builder = OppgaveBuilder()
                .withFodselsnummer(innloggetBruker.ident)
                .withGrupperingsId(dto.grupperingsid)
                .withLink(URL(dto.link))
                .withTekst(dto.tekst)
                .withTidspunkt(now)
                .withSikkerhetsnivaa(innloggetBruker.innloggingsnivaa)
                .withSynligFremTil(dto.synligFremTil?.toJavaLocalDateTime())
                .withEpostVarslingstekst(dto.epostVarslingstekst)
                .withEpostVarslingstittel(dto.epostVarslingstittel)
                .withSmsVarslingstekst(dto.smsVarslingstekst)
                .withEksternVarsling(dto.eksternVarsling)
                .withPrefererteKanaler(*getPrefererteKanaler(dto.prefererteKanaler).toTypedArray())
        return builder.build()
    }
}
