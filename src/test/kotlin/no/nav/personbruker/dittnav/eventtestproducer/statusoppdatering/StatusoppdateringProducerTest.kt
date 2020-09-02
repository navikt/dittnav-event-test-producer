package no.nav.personbruker.dittnav.eventtestproducer.statusoppdatering

import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.brukernotifikasjon.schemas.Statusoppdatering
import no.nav.personbruker.dittnav.eventtestproducer.common.InnloggetBrukerObjectMother
import no.nav.personbruker.dittnav.eventtestproducer.common.createKeyForEvent
import no.nav.personbruker.dittnav.eventtestproducer.common.kafka.KafkaProducerWrapper
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test

class StatusoppdateringProducerTest {
    private val fodselsnummer = "123"
    private val eventId = "11"
    private val systembruker = "x-dittNAV"
    private val statusGlobal = "dummyStatusGlobal"
    private val statusInternal = "dummyStatusInternal"
    private val sakstema = "dummySakstema"
    private val link = "dummyLink"
    private val innlogetBruker = InnloggetBrukerObjectMother.createInnloggetBruker(fodselsnummer)
    private val statusoppdateringKafkaProducer = mockk<KafkaProducerWrapper<Statusoppdatering>>()
    private val statusoppdateringProducer = StatusoppdateringProducer(statusoppdateringKafkaProducer, systembruker)

    @Test
    fun `should create statusoppdatering-event`() {
        runBlocking {
            val statusoppdateringDto = ProduceStatusoppdateringDto(link, statusGlobal, statusInternal, sakstema)
            val statusoppdateringKafkaEvent = statusoppdateringProducer.createStatusoppdateringForIdent(innlogetBruker, statusoppdateringDto)
            statusoppdateringKafkaEvent.getLink() `should be equal to` link
            statusoppdateringKafkaEvent.getStatusGlobal() `should be equal to` statusGlobal
            statusoppdateringKafkaEvent.getStatusIntern() `should be equal to` statusInternal
            statusoppdateringKafkaEvent.getSakstema() `should be equal to` sakstema
            statusoppdateringKafkaEvent.getFodselsnummer() `should be equal to` fodselsnummer
        }
    }

    @Test
    fun `should create statusoppdatering-key`() {
        runBlocking {
            val nokkel = createKeyForEvent(eventId, systembruker)
            nokkel.getEventId() `should be equal to` eventId
            nokkel.getSystembruker() `should be equal to` systembruker
        }
    }

}