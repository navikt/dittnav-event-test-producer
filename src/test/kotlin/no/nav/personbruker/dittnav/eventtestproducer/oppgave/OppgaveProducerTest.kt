package no.nav.personbruker.dittnav.eventtestproducer.oppgave

import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.brukernotifikasjon.schemas.Oppgave
import no.nav.personbruker.dittnav.eventtestproducer.common.InnloggetBrukerObjectMother
import no.nav.personbruker.dittnav.eventtestproducer.common.createKeyForEvent
import no.nav.personbruker.dittnav.eventtestproducer.common.kafka.KafkaProducerWrapper
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test

class OppgaveProducerTest {
    private val fodselsnummer = "123"
    private val eventId = "11"
    private val systembruker = "x-dittNAV"
    private val link = "dummyLink"
    private val tekst = "dummyTekst"
    private val innlogetBruker = InnloggetBrukerObjectMother.createInnloggetBruker(fodselsnummer)
    private val oppgaveKafkaProducer = mockk<KafkaProducerWrapper<Oppgave>>()
    private val oppgaveProducer = OppgaveProducer(oppgaveKafkaProducer, systembruker)

    @Test
    fun `should create oppgave-event`() {
        runBlocking {
            val oppgaveDto = ProduceOppgaveDto(tekst, link, true)
            val oppgaveKafkaEvent = oppgaveProducer.createOppgaveForIdent(innlogetBruker, oppgaveDto)
            oppgaveKafkaEvent.getLink() `should be equal to` link
            oppgaveKafkaEvent.getTekst() `should be equal to` tekst
            oppgaveKafkaEvent.getEksternVarsling() `should be equal to` true
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

}
