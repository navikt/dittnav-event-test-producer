package no.nav.personbruker.dittnav.eventtestproducer.oppgave

import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import no.nav.brukernotifikasjon.schemas.Nokkel
import no.nav.brukernotifikasjon.schemas.Oppgave
import no.nav.brukernotifikasjon.schemas.builders.domain.PreferertKanal
import no.nav.personbruker.dittnav.eventtestproducer.common.kafka.KafkaProducerWrapper
import no.nav.personbruker.dittnav.eventtestproducer.common.InnloggetBrukerObjectMother
import no.nav.personbruker.dittnav.eventtestproducer.common.createKeyForEvent
import org.amshove.kluent.`should be empty`
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test
import java.util.*

class OppgaveProducerTest {

    private val fodselsnummer = "12345678910"
    private val eventId = UUID.randomUUID().toString()
    private val systembruker = "x-dittNAV"
    private val link = "https://dummy.nav.no"
    private val tekst = "dummyTekst"
    private val grupperingsid = "dummyGrupperingsid"
    private val eksternVarsling = true
    private val synligFremTil = Clock.System.now().plus(7, DateTimeUnit.DAY, TimeZone.UTC)
    private val epostVarslingstekst = "<p>Du har fått en ny oppgave på Ditt NAV</p>"
    private val epostVarslingstittel = "Oppgave"
    private val smsVarslingstekst = "Du har fått en ny oppgave på Ditt NAV"
    private val prefererteKanaler = listOf(PreferertKanal.SMS.toString(), PreferertKanal.EPOST.toString())
    private val innloggetBruker = InnloggetBrukerObjectMother.createInnloggetBruker(fodselsnummer)
    private val oppgaveKafkaProducer = mockk<KafkaProducerWrapper<Nokkel, Oppgave>>()
    private val oppgaveProducer = OppgaveProducer(oppgaveKafkaProducer, systembruker)

    @Test
    fun `should create oppgave-event`() {
        runBlocking {
            val oppgaveDto = ProduceOppgaveDto(tekst, link, grupperingsid, eksternVarsling, prefererteKanaler, synligFremTil, epostVarslingstekst, epostVarslingstittel, smsVarslingstekst)
            val oppgaveKafkaEvent = oppgaveProducer.createOppgaveForIdent(innloggetBruker, oppgaveDto)
            oppgaveKafkaEvent.getLink() `should be equal to` link
            oppgaveKafkaEvent.getTekst() `should be equal to` tekst
            oppgaveKafkaEvent.getGrupperingsId() `should be equal to` grupperingsid
            oppgaveKafkaEvent.getEksternVarsling() `should be equal to` true
            oppgaveKafkaEvent.getPrefererteKanaler() `should be equal to` prefererteKanaler
            oppgaveKafkaEvent.getSynligFremTil() `should be equal to` synligFremTil.toEpochMilliseconds()
            oppgaveKafkaEvent.getEpostVarslingstekst() `should be equal to` epostVarslingstekst
            oppgaveKafkaEvent.getEpostVarslingstittel() `should be equal to` epostVarslingstittel
            oppgaveKafkaEvent.getSmsVarslingstekst() `should be equal to` smsVarslingstekst
        }
    }

    @Test
    fun `should create oppgave-key`() {
        runBlocking {
            val nokkel = createKeyForEvent(eventId, systembruker)
            nokkel.getEventId() `should be equal to` eventId
            nokkel.getSystembruker() `should be equal to` systembruker
        }
    }

    @Test
    fun `should allow no value for prefererte kanaler`() {
        val oppgaveDto = ProduceOppgaveDto(tekst, link, grupperingsid, eksternVarsling)
        val oppgaveKafkaEvent = oppgaveProducer.createOppgaveForIdent(innloggetBruker, oppgaveDto)
        oppgaveKafkaEvent.getPrefererteKanaler().`should be empty`()
    }

}
