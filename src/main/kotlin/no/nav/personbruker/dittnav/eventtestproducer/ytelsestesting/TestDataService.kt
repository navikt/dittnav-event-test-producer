package no.nav.personbruker.dittnav.eventtestproducer.ytelsestesting

import kotlinx.coroutines.delay
import no.nav.personbruker.dittnav.eventtestproducer.beskjed.BeskjedProducer
import no.nav.personbruker.dittnav.eventtestproducer.beskjed.ProduceBeskjedDto
import no.nav.personbruker.dittnav.eventtestproducer.common.InnloggetBruker
import no.nav.personbruker.dittnav.eventtestproducer.common.createKeyForEvent
import no.nav.personbruker.dittnav.eventtestproducer.done.DoneProducer
import no.nav.personbruker.dittnav.eventtestproducer.innboks.InnboksProducer
import no.nav.personbruker.dittnav.eventtestproducer.innboks.ProduceInnboksDto
import no.nav.personbruker.dittnav.eventtestproducer.oppgave.OppgaveProducer
import no.nav.personbruker.dittnav.eventtestproducer.oppgave.ProduceOppgaveDto
import no.nav.personbruker.dittnav.eventtestproducer.statusoppdatering.ProduceStatusoppdateringDto
import no.nav.personbruker.dittnav.eventtestproducer.statusoppdatering.StatusoppdateringProducer
import org.slf4j.LoggerFactory
import java.time.Instant
import java.util.*

class TestDataService(
        private val doneProducer: DoneProducer,
        private val beskjedProducer: BeskjedProducer,
        private val oppgaveProducer: OppgaveProducer,
        private val innboksProducer: InnboksProducer,
        private val statusoppdateringProducer: StatusoppdateringProducer
) {

    private val log = LoggerFactory.getLogger(TestDataService::class.java)
    val dummySystembruker = "dittnav-event-test-producer"

    suspend fun produserBeskjeder(produceDone: Boolean, yTestDto: YTestDto) {
        log.info("Produserer ${yTestDto.antallEventer} beskjeder")
        val start = Instant.now()
        val innloggetBruker = InnloggetBruker(yTestDto.ident, 4, "dummyToken")
        for (i in 1..yTestDto.antallEventer) {
            val key = createKeyForEvent(eventId = UUID.randomUUID().toString(), systembruker = dummySystembruker)
            val dto = ProduceBeskjedDto(tekst = "Beskjedtekst $i", link = "https://beskjed-$i", grupperingsid = "grupperingsid-$i", eksternVarsling = yTestDto.eksternVarsling)
            val beskjedEvent = beskjedProducer.createBeskjedInput(innloggetBruker, dto)
            beskjedProducer.sendEventToKafka(key, beskjedEvent)
            if(produceDone) {
                val doneEvent = doneProducer.createDoneEvent(innloggetBruker)
                doneProducer.sendEventToKafka(key, doneEvent)
            }
            if (isShouldTakeASmallBreakAndLogProgress(i, yTestDto.antallEventer)) {
                log.info("Har produsert beskjed nummer $i tar en liten pause")
                delay(1000)
            }
        }
        beregnBruktTid(start)
    }

    suspend fun produserOppgaver(produceDone: Boolean, yTestDto: YTestDto) {
        log.info("Produserer ${yTestDto.antallEventer} oppgaver")
        val start = Instant.now()
        val innloggetBruker = InnloggetBruker(yTestDto.ident, 4, "dummyToken")
        for (i in 1..yTestDto.antallEventer) {
            val key = createKeyForEvent(eventId = UUID.randomUUID().toString(), systembruker = dummySystembruker)
            val dto = ProduceOppgaveDto(tekst = "Oppgavetekst $i", link = "https://oppgave-$i", grupperingsid = "grupperingsid-$i", eksternVarsling = yTestDto.eksternVarsling)
            val oppgaveEvent = oppgaveProducer.createOppgaveInput(innloggetBruker, dto)
            oppgaveProducer.sendEventToKafka(key, oppgaveEvent)
            if(produceDone) {
                val doneEvent = doneProducer.createDoneEvent(innloggetBruker)
                doneProducer.sendEventToKafka(key, doneEvent)
            }
            if (isShouldTakeASmallBreakAndLogProgress(i, yTestDto.antallEventer)) {
                log.info("Har produsert oppgave nummer $i tar en liten pause")
                delay(1000)
            }
        }
        beregnBruktTid(start)
    }

    suspend fun produserInnboks(produceDone: Boolean, yTestDto: YTestDto) {
        log.info("Produserer ${yTestDto.antallEventer} innboks-eventer")
        val start = Instant.now()
        val innloggetBruker = InnloggetBruker(yTestDto.ident, 4, "dummyToken")
        for (i in 1..yTestDto.antallEventer) {
            val key = createKeyForEvent("i-$i", dummySystembruker)
            val dto = ProduceInnboksDto("Innbokstekst $i", "https://innboks-$i", "grupperingsid-$i")
            val innboksEvent = innboksProducer.createInnboksInput(innloggetBruker, dto)
            innboksProducer.sendEventToKafka(key, innboksEvent)
            if(produceDone) {
                val doneEvent = doneProducer.createDoneEvent(innloggetBruker)
                doneProducer.sendEventToKafka(key, doneEvent)
            }
            if (isShouldTakeASmallBreakAndLogProgress(i, yTestDto.antallEventer)) {
                log.info("Har produsert innboks-event nummer $i tar en liten pause")
                delay(1000)
            }
        }
        beregnBruktTid(start)
    }

    suspend fun produserStatusoppdateringer(yTestDto: YTestDto) {
        log.info("Produserer ${yTestDto.antallEventer} Statusoppdatering-eventer")
        val start = Instant.now()
        val innloggetBruker = InnloggetBruker(yTestDto.ident, 4, "dummyToken")
        for (i in 1..yTestDto.antallEventer) {
            val key = createKeyForEvent("s-$i", dummySystembruker)
            val dto = ProduceStatusoppdateringDto("https://dummyLink_$i", "SENDT", "dummyStatusIntern_$i", "dummySakstema_$i", "grupperingsid-$i")
            val statusoppdateringEvent = statusoppdateringProducer.createStatusoppdateringInput(innloggetBruker, dto)
            statusoppdateringProducer.sendEventToKafka(key, statusoppdateringEvent)
            if (isShouldTakeASmallBreakAndLogProgress(i, yTestDto.antallEventer)) {
                log.info("Har produsert Statusoppdatering-event nummer $i tar en liten pause")
                delay(1000)
            }
        }
        beregnBruktTid(start)
    }

    private fun beregnBruktTid(start: Instant) {
        val stop = Instant.now()
        val tidbrukt = stop.minusMillis(start.toEpochMilli()).toEpochMilli()
        val tidbruktISekunder = tidbrukt / 1000
        log.info("Gjenbrukt kafka-produsent med venting, produseringen tok: $tidbruktISekunder sekunder.\n")
    }

    private fun isShouldTakeASmallBreakAndLogProgress(i: Int, antallEventer: Int) = i % (antallEventer / 10) == 0

}
