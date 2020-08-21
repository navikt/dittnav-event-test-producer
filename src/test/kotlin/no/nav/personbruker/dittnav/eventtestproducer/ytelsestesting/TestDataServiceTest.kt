package no.nav.personbruker.dittnav.eventtestproducer.ytelsestesting

import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.eventtestproducer.beskjed.BeskjedProducer
import no.nav.personbruker.dittnav.eventtestproducer.config.Environment
import no.nav.personbruker.dittnav.eventtestproducer.done.DoneProducer
import no.nav.personbruker.dittnav.eventtestproducer.innboks.InnboksProducer
import no.nav.personbruker.dittnav.eventtestproducer.oppgave.OppgaveProducer
import no.nav.personbruker.dittnav.eventtestproducer.statusoppdatering.StatusoppdateringProducer

internal class TestDataServiceTest {

    private val env = createPropertiesForTestEnvironment()

    private val doneProducer = DoneProducer(env)
    private val beskjedProducer = BeskjedProducer(env)
    private val innboksProducer = InnboksProducer(env)
    private val oppgaveProducer = OppgaveProducer(env)
    private val statusoppdateringProducer = StatusoppdateringProducer(env)

    // @Test
    fun produserTestCase() {
        val testDataService = TestDataService(doneProducer, beskjedProducer, oppgaveProducer, innboksProducer, statusoppdateringProducer)
        runBlocking {
            testDataService.produserBeskjederOgTilhorendeDoneEventer()
            testDataService.produserOppgaveOgTilhorendeDoneEventer()
            testDataService.produserInnboksOgTilhorendeDoneEventer()
            testDataService.produserStatusoppdateringsEventer()
        }
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
