package no.nav.personbruker.dittnav.eventtestproducer.beskjed

import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.brukernotifikasjon.schemas.Beskjed
import no.nav.personbruker.dittnav.eventtestproducer.common.InnloggetBrukerObjectMother
import no.nav.personbruker.dittnav.eventtestproducer.common.createKeyForEvent
import no.nav.personbruker.dittnav.eventtestproducer.common.kafka.KafkaProducerWrapper
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test

class BeskjedProducerTest {
    private val fodselsnummer = "123"
    private val eventId = "11"
    private val systembruker = "x-dittNAV"
    private val link = "dummyLink"
    private val tekst = "dummyTekst"
    private val grupperingsid = "dummyGrupperingsid"
    private val innlogetBruker = InnloggetBrukerObjectMother.createInnloggetBruker(fodselsnummer)
    private val beskjedKafkaProducer = mockk<KafkaProducerWrapper<Beskjed>>()
    private val beskjedProducer = BeskjedProducer(beskjedKafkaProducer, systembruker)

    @Test
    fun `should create beskjed-event`() {
        runBlocking {
            val beskjedDto = ProduceBeskjedDto(tekst, link, grupperingsid)
            val beskjedKafkaEvent = beskjedProducer.createBeskjedForIdent(innlogetBruker, beskjedDto)
            beskjedKafkaEvent.getLink() `should be equal to` link
            beskjedKafkaEvent.getTekst() `should be equal to` tekst
            beskjedKafkaEvent.getGrupperingsId() `should be equal to` grupperingsid
        }
    }

    @Test
    fun `should create beskjed-key`() {
        runBlocking {
            val nokkel = createKeyForEvent(eventId, systembruker)
            nokkel.getEventId() `should be equal to` eventId
            nokkel.getSystembruker() `should be equal to` systembruker
        }
    }

}