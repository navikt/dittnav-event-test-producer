package no.nav.personbruker.dittnav.eventtestproducer.statusoppdatering

import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.brukernotifikasjon.schemas.Nokkel
import no.nav.brukernotifikasjon.schemas.Statusoppdatering
import no.nav.personbruker.dittnav.eventtestproducer.common.kafka.KafkaProducerWrapper
import no.nav.personbruker.dittnav.eventtestproducer.common.InnloggetBrukerObjectMother
import no.nav.personbruker.dittnav.eventtestproducer.common.createKeyForEvent
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test
import java.util.*

class StatusoppdateringProducerTest {
    private val fodselsnummer = "12345678910"
    private val eventId = UUID.randomUUID().toString()
    private val systembruker = "x-dittNAV"
    private val statusGlobal = "FERDIG"
    private val statusInternal = "dummyStatusInternal"
    private val sakstema = "dummySakstema"
    private val link = "https://dummy.nav.no"
    private val grupperingsid = "dummyGrupperingsid"
    private val innlogetBruker = InnloggetBrukerObjectMother.createInnloggetBruker(fodselsnummer)
    private val statusoppdateringKafkaProducer = mockk<KafkaProducerWrapper<Nokkel, Statusoppdatering>>()
    private val statusoppdateringProducer = StatusoppdateringProducer(statusoppdateringKafkaProducer, systembruker)

    @Test
    fun `should create statusoppdatering-event`() {
        runBlocking {
            val statusoppdateringDto = ProduceStatusoppdateringDto(link, statusGlobal, statusInternal, sakstema, grupperingsid)
            val statusoppdateringKafkaEvent = statusoppdateringProducer.createStatusoppdateringForIdent(innlogetBruker, statusoppdateringDto)
            statusoppdateringKafkaEvent.getLink() `should be equal to` link
            statusoppdateringKafkaEvent.getStatusGlobal() `should be equal to` statusGlobal
            statusoppdateringKafkaEvent.getStatusIntern() `should be equal to` statusInternal
            statusoppdateringKafkaEvent.getSakstema() `should be equal to` sakstema
            statusoppdateringKafkaEvent.getFodselsnummer() `should be equal to` fodselsnummer
            statusoppdateringKafkaEvent.getGrupperingsId() `should be equal to` grupperingsid
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
