package no.nav.personbruker.dittnav.eventtestproducer.common

import no.nav.brukernotifikasjon.schemas.Nokkel
import no.nav.personbruker.dittnav.eventtestproducer.beskjed.BeskjedProducer
import no.nav.personbruker.dittnav.eventtestproducer.beskjed.ProduceBeskjedDto
import no.nav.personbruker.dittnav.eventtestproducer.config.Environment
import no.nav.personbruker.dittnav.eventtestproducer.done.DoneProducer
import no.nav.personbruker.dittnav.eventtestproducer.oppgave.OppgaveProducer
import no.nav.personbruker.dittnav.eventtestproducer.oppgave.ProduceOppgaveDto
import org.amshove.kluent.`should be equal to`
import org.junit.Test
import java.time.Instant

class ProduserEventerForYtelestest {

    private val env = createPropertiesForTestEnvironment()

    private val bruker = InnloggetBruker("88888", 4, "dummyToken")
    private val dummySystembruker = "test-producer"

    private val doneProducer = DoneProducer(env)

    private val brukteKeys = mutableListOf<Nokkel>()

    private val antallEventer = 50000

    @Test
    fun `produser 50000 oppgaver og beskjeder, og et tilhorende done-event for hver av de`() {
        produserBeskjeder()
        produserOppgave()
        produceDoneEventsForAlleBeskjederOgOppgaver()

        println("...er ferdig med å produsere eventer.")
    }

    private fun produserBeskjeder() {
        println("Produserer $antallEventer beskjeder")
        val beskjedProducer = BeskjedProducer(env)
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
        val oppgaveProducer = OppgaveProducer(env)
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
        var counter = 0
        brukteKeys.forEach { key ->
            val value = doneProducer.createDoneEvent(bruker)
            doneProducer.produceEvent(bruker, key, value)
            counter++
        }
        counter `should be equal to` (antallEventer * 2)
    }

    private fun beregnBruktTid(start: Instant) {
        val stop = Instant.now()
        val tidbrukt = stop.minusMillis(start.toEpochMilli()).toEpochMilli()
        val tidbruktISekunder = tidbrukt / 1000
        println("Gjenbrukt kafka-produsent med venting, produseringen tok: $tidbruktISekunder sekunder.\n")
    }

    private fun createPropertiesForTestEnvironment(): Environment {
        return Environment(
                bootstrapServers = "localhost:29092",
                schemaRegistryUrl = "http://localhost:8081",
                systemUserName = "username",
                systemUserPassword = "password",
                dbHost = "localhost:5432",
                dbName = "dittnav-event-cache-preprod",
                dbMountPath = "notUsedOnLocalhost",
                corsAllowedOrigins = "localhost:9002"
        )
    }

}
