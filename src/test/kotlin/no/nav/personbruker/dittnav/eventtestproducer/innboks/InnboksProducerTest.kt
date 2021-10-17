package no.nav.personbruker.dittnav.eventtestproducer.innboks

import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.brukernotifikasjon.schemas.Nokkel
import no.nav.brukernotifikasjon.schemas.builders.domain.PreferertKanal
import no.nav.personbruker.dittnav.eventtestproducer.common.InnloggetBrukerObjectMother
import no.nav.personbruker.dittnav.eventtestproducer.common.createKeyForEvent
import no.nav.personbruker.dittnav.eventtestproducer.common.kafka.KafkaProducerWrapper
import org.amshove.kluent.`should be empty`
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test
import java.util.*

class InnboksProducerTest {

    private val fodselsnummer = "12345678910"
    private val eventId = UUID.randomUUID().toString()
    private val systembruker = "x-dittNAV"
    private val link = "https://dummy.nav.no"
    private val tekst = "dummyTekst"
    private val grupperingsid = "dummyGrupperingsid"
    private val eksternVarsling = true
    private val prefererteKanaler = listOf(PreferertKanal.SMS.toString(), PreferertKanal.EPOST.toString())
    private val innloggetBruker = InnloggetBrukerObjectMother.createInnloggetBruker(fodselsnummer)
    private val innboksKafkaProducer = mockk<KafkaProducerWrapper<Nokkel, Innboks>>()
    private val innboksProducer = InnboksProducer(innboksKafkaProducer, systembruker)

    @Test
    fun `should create innboks-event`() {
        runBlocking {
            val innboksDto = ProduceInnboksDto(tekst, link, grupperingsid, eksternVarsling, prefererteKanaler)
            val innboksKafkaEvent = innboksProducer.createInnboksInput(innloggetBruker, innboksDto)
            innboksKafkaEvent.getLink() `should be equal to` link
            innboksKafkaEvent.getTekst() `should be equal to` tekst
            innboksKafkaEvent.getGrupperingsId() `should be equal to` grupperingsid
            innboksKafkaEvent.getEksternVarsling() `should be equal to` true
            innboksKafkaEvent.getPrefererteKanaler() `should be equal to` prefererteKanaler
        }
    }

    @Test
    fun `should create innboks-key`() {
        runBlocking {
            val nokkel = createKeyForEvent(eventId, systembruker)
            nokkel.getEventId() `should be equal to` eventId
            nokkel.getSystembruker() `should be equal to` systembruker
        }
    }

    @Test
    fun `should allow no value for prefererte kanaler`() {
        val innboksDto = ProduceInnboksDto(tekst, link, grupperingsid, eksternVarsling)
        val innboksKafkaEvent = innboksProducer.createInnboksInput(innloggetBruker, innboksDto)
        innboksKafkaEvent.getPrefererteKanaler().`should be empty`()
    }

}
