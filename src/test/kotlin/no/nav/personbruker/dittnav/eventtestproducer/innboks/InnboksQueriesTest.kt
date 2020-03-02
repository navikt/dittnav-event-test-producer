package no.nav.personbruker.dittnav.eventtestproducer.innboks

import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.eventtestproducer.common.InnloggetBrukerObjectMother
import no.nav.personbruker.dittnav.eventtestproducer.common.database.H2Database
import org.amshove.kluent.`should be empty`
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test

class InnboksQueriesTest {
    private val database = H2Database()
    private val innloggetBruker = InnloggetBrukerObjectMother.createInnloggetBrukerMedInnloggingsnivaa4()

    @Test
    fun `Finn alle cachede Innboks-eventer for fodselsnummer`() {
        runBlocking {
            database.dbQuery { getAllInnboksByFodselsnummer(innloggetBruker) }.size `should be equal to` 3
        }
    }

    @Test
    fun `Finner kun aktive cachede Innboks-eventer for fodselsnummer`() {
        runBlocking {
            database.dbQuery { getInnboksByFodselsnummer(innloggetBruker) }.size `should be equal to` 2
        }
    }

    @Test
    fun `Returnerer tom liste hvis Innboks-eventer for fodselsnummer ikke finnes`() {
        val brukerUtenEventer = InnloggetBrukerObjectMother.createInnloggetBrukerWithSubject("456")

        runBlocking {
            database.dbQuery { getInnboksByFodselsnummer(brukerUtenEventer) }.`should be empty`()
        }
    }

    @Test
    fun `Returnerer tom liste hvis Innboks-eventer hvis tom fodselsnummer`() {
        val brukerUtenFodselsnummer = InnloggetBrukerObjectMother.createInnloggetBrukerWithSubject("")

        runBlocking {
            database.dbQuery { getInnboksByFodselsnummer(brukerUtenFodselsnummer) }.`should be empty`()
        }
    }
}