package no.nav.personbruker.dittnav.eventtestproducer.oppgave

import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.eventtestproducer.common.database.H2Database
import org.junit.jupiter.api.Test
import org.amshove.kluent.*

class OppgaveQueriesTest {

    private val database = H2Database()

    @Test
    fun `Finn alle cachede Oppgave-eventer for aktorID`() {
        runBlocking {
            database.dbQuery { getAllOppgaveByAktorId("12345") }.size `should be equal to` 3
        }
    }

    @Test
    fun `Finn alle aktive cachede Oppgave-eventer for aktorID`() {
        runBlocking {
            database.dbQuery { getOppgaveByAktorId("12345") }.size `should be equal to` 2
        }
    }

    @Test
    fun `Returnerer tom liste hvis Oppgave-eventer for aktorID ikke finnes`() {
        runBlocking {
            database.dbQuery { getOppgaveByAktorId("finnesikke") }.isEmpty()
        }
    }

    @Test
    fun `Returnerer tom liste hvis Oppgave-eventer hvis tom aktorID`() {
        runBlocking {
            database.dbQuery { getOppgaveByAktorId("") }.isEmpty()
        }
    }
}
