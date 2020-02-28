package no.nav.personbruker.dittnav.eventtestproducer.beskjed

import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.eventtestproducer.common.InnloggetBrukerObjectMother
import no.nav.personbruker.dittnav.eventtestproducer.common.database.H2Database
import org.amshove.kluent.`should be empty`
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test

class BeskjedQueriesTest {

    private val database = H2Database()
    private val innloggetBruker = InnloggetBrukerObjectMother.createInnloggetBrukerMedInnloggingsnivaa4()

    @Test
    fun `Finn alle cachede Beskjed-eventer for fodselsnummer`() {
        runBlocking {
            database.dbQuery { getAllBeskjedByFodselsnummer(innloggetBruker) }.size `should be equal to` 3
        }
    }

    @Test
    fun `Finner kun aktive cachede Beskjed-eventer for fodselsnummer`() {
        runBlocking {
            database.dbQuery { getBeskjedByFodselsnummer(innloggetBruker) }.size `should be equal to` 2
        }
    }

    @Test
    fun `Returnerer tom liste hvis Beskjed-eventer for fodselsnummer ikke finnes`() {
        val brukerUtenEventer = InnloggetBrukerObjectMother.createInnloggetBrukerWithSubject("456")

        runBlocking {
            database.dbQuery { getBeskjedByFodselsnummer(brukerUtenEventer) }.`should be empty`()
        }
    }


    @Test
    fun `Returnerer tom liste hvis Beskjed-eventer hvis tom fodselsnummer`() {
        val brukerUtenFodselsnummer = InnloggetBrukerObjectMother.createInnloggetBrukerWithSubject("")

        runBlocking {
            database.dbQuery { getBeskjedByFodselsnummer(brukerUtenFodselsnummer) }.`should be empty`()
        }
    }
}
