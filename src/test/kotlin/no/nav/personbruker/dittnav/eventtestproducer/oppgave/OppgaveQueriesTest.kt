package no.nav.personbruker.dittnav.eventtestproducer.oppgave

import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.eventtestproducer.common.database.H2Database
import org.junit.jupiter.api.Test
import org.amshove.kluent.*

class OppgaveQueriesTest {

    private val database = H2Database()

    @Test
    fun `Finn alle cachede Oppgave-eventer for fodselsnummer`() {
        runBlocking {
            database.dbQuery { getAllOppgaveByFodselsnummer("12345") }.size `should be equal to` 3
        }
    }

    @Test
    fun `Finn alle aktive cachede Oppgave-eventer for fodselsnummer`() {
        runBlocking {
            database.dbQuery { getOppgaveByFodselsnummer("12345") }.size `should be equal to` 2
        }
    }

    @Test
    fun `Returnerer tom liste hvis Oppgave-eventer for fodselsnummer ikke finnes`() {
        runBlocking {
            database.dbQuery { getOppgaveByFodselsnummer("finnesikke") }.isEmpty()
        }
    }

    @Test
    fun `Returnerer tom liste hvis Oppgave-eventer hvis tom fodselsnummer`() {
        runBlocking {
            database.dbQuery { getOppgaveByFodselsnummer("") }.isEmpty()
        }
    }
}
