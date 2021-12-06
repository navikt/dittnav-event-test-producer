package no.nav.personbruker.dittnav.eventtestproducer.beskjed

import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import no.nav.brukernotifikasjon.schemas.Beskjed
import no.nav.brukernotifikasjon.schemas.Nokkel
import no.nav.brukernotifikasjon.schemas.builders.domain.PreferertKanal
import no.nav.personbruker.dittnav.eventtestproducer.common.InnloggetBrukerObjectMother
import no.nav.personbruker.dittnav.eventtestproducer.common.createKeyForEvent
import no.nav.personbruker.dittnav.eventtestproducer.common.kafka.KafkaProducerWrapper
import org.amshove.kluent.`should be empty`
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test
import java.util.*

class BeskjedProducerTest {

    private val synligFremTil = Clock.System.now().plus(7, DateTimeUnit.DAY, TimeZone.UTC).toLocalDateTime(TimeZone.UTC)
    private val fodselsnummer = "12345678910"
    private val eventId = UUID.randomUUID().toString()
    private val systembruker = "x-dittNAV"
    private val link = "https://dummy.nav.no"
    private val tekst = "dummyTekst"
    private val grupperingsid = "dummyGrupperingsid"
    private val eksternVarsling = true
    private val prefererteKanaler = listOf(PreferertKanal.SMS.toString(), PreferertKanal.EPOST.toString())
    private val innloggetBruker = InnloggetBrukerObjectMother.createInnloggetBruker(fodselsnummer)
    private val beskjedKafkaProducer = mockk<KafkaProducerWrapper<Nokkel, Beskjed>>()
    private val beskjedProducer = BeskjedProducer(beskjedKafkaProducer, systembruker)

    @Test
    fun `should create beskjed-event`() {
        runBlocking {
            val beskjedDto = ProduceBeskjedDto(tekst, link, grupperingsid, eksternVarsling, prefererteKanaler, synligFremTil)
            val beskjedKafkaEvent = beskjedProducer.createBeskjedForIdent(innloggetBruker, beskjedDto)
            beskjedKafkaEvent.getLink() `should be equal to` link
            beskjedKafkaEvent.getTekst() `should be equal to` tekst
            beskjedKafkaEvent.getGrupperingsId() `should be equal to` grupperingsid
            beskjedKafkaEvent.getEksternVarsling() `should be equal to` true
            beskjedKafkaEvent.getPrefererteKanaler() `should be equal to` prefererteKanaler
            beskjedKafkaEvent.getSynligFremTil() `should be equal to` synligFremTil.toInstant(TimeZone.UTC).toEpochMilliseconds()
        }
    }

    @Test
    fun `should allow no synligFremTil value`() {
        val beskjedDto = ProduceBeskjedDto(tekst, link, grupperingsid, eksternVarsling, prefererteKanaler, synligFremTil = null)
        val beskjedKafkaEvent = beskjedProducer.createBeskjedForIdent(innloggetBruker, beskjedDto)
        beskjedKafkaEvent.getSynligFremTil() `should be equal to` null
    }

    @Test
    fun `should allow no link value`() {
        val beskjedDto = ProduceBeskjedDto(tekst, null, grupperingsid, eksternVarsling)
        val beskjedKafkaEvent = beskjedProducer.createBeskjedForIdent(innloggetBruker, beskjedDto)
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

    @Test
    fun `should allow no value for prefererte kanaler`() {
        val beskjedDto = ProduceBeskjedDto(tekst, link, grupperingsid, eksternVarsling)
        val beskjedKafkaEvent = beskjedProducer.createBeskjedForIdent(innloggetBruker, beskjedDto)
        beskjedKafkaEvent.getPrefererteKanaler().`should be empty`()
    }

}
