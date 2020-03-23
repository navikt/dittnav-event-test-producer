package no.nav.personbruker.dittnav.eventtestproducer.ytelsestesting

import no.nav.brukernotifikasjon.schemas.Nokkel
import no.nav.personbruker.dittnav.eventtestproducer.beskjed.BeskjedProducer
import no.nav.personbruker.dittnav.eventtestproducer.beskjed.ProduceBeskjedDto
import no.nav.personbruker.dittnav.eventtestproducer.common.InnloggetBruker
import no.nav.personbruker.dittnav.eventtestproducer.common.createKeyForEvent
import no.nav.personbruker.dittnav.eventtestproducer.done.DoneProducer
import no.nav.personbruker.dittnav.eventtestproducer.oppgave.OppgaveProducer
import no.nav.personbruker.dittnav.eventtestproducer.oppgave.ProduceOppgaveDto
import java.time.Instant

class TestDataService(
        private val doneProducer: DoneProducer,
        private val beskjedProducer: BeskjedProducer,
        private val oppgaveProducer: OppgaveProducer
) {

    private val bruker = InnloggetBruker("88888", 4, "dummyToken")
    private val dummySystembruker = "test-producer"

    private var brukteKeys = mutableListOf<Nokkel>()
    private val antallEventer = 50000

    fun produserTestCase() {
        brukteKeys = mutableListOf()
        produserBeskjeder()
        produserOppgave()
        produceDoneEventsForAlleBeskjederOgOppgaver()

        println("...er ferdig med å produsere eventer.")
    }

    private fun produserBeskjeder() {
        println("Produserer $antallEventer beskjeder")
        val start = Instant.now()
        for (i in 1..antallEventer) {
            val key = createKeyForEvent("b-$i", dummySystembruker)
            brukteKeys.add(key)
            val beskjed = ProduceBeskjedDto("Beskjedtekst $i", "https://beskjed-$i")
            val value = beskjedProducer.createBeskjedForIdent(bruker, beskjed)
            beskjedProducer.produceEvent(bruker, key, value)
            if (i % 10000 == 0) {
                println("Har produsert beskjed nummer $i tar en liten pause")
                Thread.sleep(1000)
            }
        }
        beregnBruktTid(start)
    }

    private fun produserOppgave() {
        println("Produserer $antallEventer oppgaver")
        val start = Instant.now()
        for (i in 1..antallEventer) {
            val key = createKeyForEvent("o-$i", dummySystembruker)
            brukteKeys.add(key)
            val oppgave = ProduceOppgaveDto("Oppgavetekst $i", "https://oppgave-$i")
            val value = oppgaveProducer.createOppgaveForIdent(bruker, oppgave)
            oppgaveProducer.produceEvent(bruker, key, value)
            if (i % 10000 == 0) {
                println("Har produsert beskjed nummer $i tar en liten pause")
                Thread.sleep(1000)
            }
        }
        beregnBruktTid(start)
    }

    private fun produceDoneEventsForAlleBeskjederOgOppgaver() {
        println("Produserer ${brukteKeys.size} done-eventer, en til hver beskjed og oppgave")
        var counter = 0
        val start = Instant.now()
        brukteKeys.forEach { key ->
            val value = doneProducer.createDoneEvent(bruker)
            doneProducer.produceEvent(bruker, key, value)
            counter++
            if (counter % 10000 == 0) {
                println("Har produsert done-event nummer $counter tar en liten pause")
                Thread.sleep(1000)
            }
        }
        beregnBruktTid(start)
    }

    private fun beregnBruktTid(start: Instant) {
        val stop = Instant.now()
        val tidbrukt = stop.minusMillis(start.toEpochMilli()).toEpochMilli()
        val tidbruktISekunder = tidbrukt / 1000
        println("Gjenbrukt kafka-produsent med venting, produseringen tok: $tidbruktISekunder sekunder.\n")
    }

}
