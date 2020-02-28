package no.nav.personbruker.dittnav.eventtestproducer.oppgave

import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.eventtestproducer.common.InnloggetBrukerObjectMother
import no.nav.personbruker.dittnav.eventtestproducer.common.database.H2Database
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test

class OppgaveQueriesTest {

    private val database = H2Database()
    private val innloggetBruker = InnloggetBrukerObjectMother.createInnloggetBrukerMedInnloggingsnivaa4()

    @Test
    fun `Finn alle cachede Oppgave-eventer for fodselsnummer`() {
        runBlocking {
            database.dbQuery { getAllOppgaveByFodselsnummer(innloggetBruker) }.size `should be equal to` 3
        }
    }

    @Test
    fun `Finn alle aktive cachede Oppgave-eventer for fodselsnummer`() {
        runBlocking {
            database.dbQuery { getOppgaveByFodselsnummer(innloggetBruker) }.size `should be equal to` 2
        }
    }

    @Test
    fun `Returnerer tom liste hvis Oppgave-eventer for fodselsnummer ikke finnes`() {
        val bruker = InnloggetBrukerObjectMother.createInnloggetBrukerWithSubject("456")

        runBlocking {
            database.dbQuery { getOppgaveByFodselsnummer(bruker) }.isEmpty()
        }
    }

    @Test
    fun `Returnerer tom liste hvis Oppgave-eventer hvis tom fodselsnummer`() {
        val brukerUtenFodselsnummer = InnloggetBrukerObjectMother.createInnloggetBrukerWithSubject("")

        runBlocking {
            database.dbQuery { getOppgaveByFodselsnummer(brukerUtenFodselsnummer) }.isEmpty()
        }
    }
}
