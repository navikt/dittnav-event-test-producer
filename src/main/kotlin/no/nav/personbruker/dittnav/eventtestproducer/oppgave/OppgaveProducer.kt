package no.nav.personbruker.dittnav.eventtestproducer.oppgave

import no.nav.brukernotifikasjon.schemas.Nokkel
import no.nav.brukernotifikasjon.schemas.Oppgave
import no.nav.personbruker.dittnav.eventtestproducer.common.InnloggetBruker
import no.nav.personbruker.dittnav.eventtestproducer.common.createKeyForEvent
import no.nav.personbruker.dittnav.eventtestproducer.common.kafka.KafkaProducerWrapper
import org.slf4j.LoggerFactory
import java.time.Instant

class OppgaveProducer(private val oppgaveKafkaProducer: KafkaProducerWrapper<Oppgave>, private val systembruker: String) {

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
        val nowInMs = Instant.now().toEpochMilli()
        val build = Oppgave.newBuilder()
                .setFodselsnummer(innloggetBruker.ident)
                .setGrupperingsId(dto.grupperingsid)
                .setLink(dto.link)
                .setTekst(dto.tekst)
                .setTidspunkt(nowInMs)
                .setSikkerhetsnivaa(innloggetBruker.innloggingsnivaa)
                .setEksternVarsling(dto.eksternVarsling)
        return build.build()
    }
}
