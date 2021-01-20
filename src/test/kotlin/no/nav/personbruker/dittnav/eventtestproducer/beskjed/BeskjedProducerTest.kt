package no.nav.personbruker.dittnav.eventtestproducer.beskjed

import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.brukernotifikasjon.schemas.Beskjed
import no.nav.brukernotifikasjon.schemas.Nokkel
import no.nav.personbruker.dittnav.common.util.kafka.producer.KafkaProducerWrapper
import no.nav.personbruker.dittnav.eventtestproducer.common.InnloggetBrukerObjectMother
import no.nav.personbruker.dittnav.eventtestproducer.common.createKeyForEvent
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should be null`
import org.junit.jupiter.api.Test
import java.util.*

class BeskjedProducerTest {
    private val fodselsnummer = "12345678910"
    private val eventId = UUID.randomUUID().toString()
    private val systembruker = "x-dittNAV"
    private val link = "https://dummy.nav.no"
    private val tekst = "dummyTekst"
    private val grupperingsid = "dummyGrupperingsid"
    private val eksternVarsling = true
    private val innlogetBruker = InnloggetBrukerObjectMother.createInnloggetBruker(fodselsnummer)
    private val beskjedKafkaProducer = mockk<KafkaProducerWrapper<Nokkel, Beskjed>>()
    private val beskjedProducer = BeskjedProducer(beskjedKafkaProducer, systembruker)

    @Test
    fun `should create beskjed-event`() {
        runBlocking {
            val beskjedDto = ProduceBeskjedDto(tekst, link, grupperingsid, eksternVarsling)
            val beskjedKafkaEvent = beskjedProducer.createBeskjedForIdent(innlogetBruker, beskjedDto)
            beskjedKafkaEvent.getLink() `should be equal to` link
            beskjedKafkaEvent.getTekst() `should be equal to` tekst
            beskjedKafkaEvent.getGrupperingsId() `should be equal to` grupperingsid
            beskjedKafkaEvent.getEksternVarsling() `should be equal to` true
        }
    }

    @Test
    fun `should allow no link value`() {
        val beskjedDto = ProduceBeskjedDto(tekst, null, grupperingsid, eksternVarsling)
        val beskjedKafkaEvent = beskjedProducer.createBeskjedForIdent(innlogetBruker, beskjedDto)
        beskjedKafkaEvent.getLink() `should be equal to` ""
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
