package no.nav.personbruker.dittnav.eventtestproducer.beskjed

import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.eventtestproducer.common.InnloggetBrukerObjectMother
import no.nav.personbruker.dittnav.eventtestproducer.common.database.H2Database
import org.amshove.kluent.`should be empty`
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.time.ZonedDateTime

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BeskjedQueriesTest {

    private val database = H2Database()
    private val innloggetBruker = InnloggetBrukerObjectMother.createInnloggetBruker()

    private val beskjed1 = BeskjedObjectMother.createBeskjed(id = 1, eventId = "123", fodselsnummer = innloggetBruker.ident,
            synligFremTil = ZonedDateTime.now().plusHours(1), uid = "11", aktiv = true)
    private val beskjed2 = BeskjedObjectMother.createBeskjed(id = 2, eventId = "124", fodselsnummer = innloggetBruker.ident,
            synligFremTil = ZonedDateTime.now().plusHours(1), uid = "22", aktiv = true)
    private val beskjed3 = BeskjedObjectMother.createBeskjed(id = 3, eventId = "125", fodselsnummer = innloggetBruker.ident,
            synligFremTil = ZonedDateTime.now().plusHours(1), uid = "33", aktiv = false)
    private val beskjed4 = BeskjedObjectMother.createBeskjed(id = 4, eventId = "126", fodselsnummer = "54321",
            synligFremTil = ZonedDateTime.now().plusHours(1), uid = "44", aktiv = true)

    @BeforeAll
    fun `populer testdata`() {
        runBlocking {
            database.dbQuery { createBeskjed(listOf(beskjed1, beskjed2, beskjed3, beskjed4)) }
        }
    }

    @AfterAll
    fun `slett testdata`() {
        runBlocking {
            database.dbQuery { deleteBeskjed(listOf(beskjed1, beskjed2, beskjed3, beskjed4)) }
        }
    }

    @Test
    fun `Finn alle cachede Beskjed-eventer for fodselsnummer`() {
        runBlocking {
            database.dbQuery { getAllBeskjedByFodselsnummer(innloggetBruker) }.size `should be equal to` 3
        }
    }

    @Test
    fun `Finner kun aktive cachede Beskjed-eventer for fodselsnummer`() {
        runBlocking {
            database.dbQuery { getAktivBeskjedByFodselsnummer(innloggetBruker) }.size `should be equal to` 2
        }
    }

    @Test
    fun `Returnerer tom liste hvis Beskjed-eventer for fodselsnummer ikke finnes`() {
        val brukerUtenEventer = InnloggetBrukerObjectMother.createInnloggetBruker("456")
        runBlocking {
            database.dbQuery { getAktivBeskjedByFodselsnummer(brukerUtenEventer) }.`should be empty`()
        }
    }

    @Test
    fun `Returnerer tom liste hvis Beskjed-eventer hvis tom fodselsnummer`() {
        val brukerUtenFodselsnummer = InnloggetBrukerObjectMother.createInnloggetBruker("")
        runBlocking {
            database.dbQuery { getAktivBeskjedByFodselsnummer(brukerUtenFodselsnummer) }.`should be empty`()
        }
    }
}
