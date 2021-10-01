package no.nav.personbruker.dittnav.eventtestproducer.oppgave

import io.mockk.mockk
import kotlinx.coroutines.runBlocking
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
    private val prefererteKanaler = listOf(PreferertKanal.SMS.toString(), PreferertKanal.EPOST.toString())
    private val innloggetBruker = InnloggetBrukerObjectMother.createInnloggetBruker(fodselsnummer)
    private val oppgaveKafkaProducer = mockk<KafkaProducerWrapper<Nokkel, Oppgave>>()
    private val oppgaveProducer = OppgaveProducer(oppgaveKafkaProducer, systembruker)

    @Test
    fun `should create oppgave-event`() {
        runBlocking {
            val oppgaveDto = ProduceOppgaveDto(tekst, link, grupperingsid, eksternVarsling, prefererteKanaler)
            val oppgaveKafkaEvent = oppgaveProducer.createOppgaveForIdent(innloggetBruker, oppgaveDto)
            oppgaveKafkaEvent.getLink() `should be equal to` link
            oppgaveKafkaEvent.getTekst() `should be equal to` tekst
            oppgaveKafkaEvent.getGrupperingsId() `should be equal to` grupperingsid
            oppgaveKafkaEvent.getEksternVarsling() `should be equal to` true
            oppgaveKafkaEvent.getPrefererteKanaler() `should be equal to` prefererteKanaler
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
