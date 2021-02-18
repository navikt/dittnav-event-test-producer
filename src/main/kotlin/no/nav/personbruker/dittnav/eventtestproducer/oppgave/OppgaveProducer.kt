package no.nav.personbruker.dittnav.eventtestproducer.oppgave

import no.nav.brukernotifikasjon.schemas.Nokkel
import no.nav.brukernotifikasjon.schemas.Oppgave
import no.nav.brukernotifikasjon.schemas.builders.OppgaveBuilder
import no.nav.personbruker.dittnav.eventtestproducer.common.kafka.KafkaProducerWrapper
import no.nav.personbruker.dittnav.eventtestproducer.common.InnloggetBruker
import no.nav.personbruker.dittnav.eventtestproducer.common.createKeyForEvent
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
        val build = OppgaveBuilder()
                .withFodselsnummer(innloggetBruker.ident)
                .withGrupperingsId(dto.grupperingsid)
                .withLink(URL(dto.link))
                .withTekst(dto.tekst)
                .withTidspunkt(now)
                .withSikkerhetsnivaa(innloggetBruker.innloggingsnivaa)
                .withEksternVarsling(dto.eksternVarsling)
        return build.build()
    }
}
