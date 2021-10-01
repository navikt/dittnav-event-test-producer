package no.nav.personbruker.dittnav.eventtestproducer.innboks

import kotlinx.coroutines.runBlocking
import no.nav.brukernotifikasjon.schemas.builders.domain.PreferertKanal
import no.nav.personbruker.dittnav.eventtestproducer.common.InnloggetBrukerObjectMother
import no.nav.personbruker.dittnav.eventtestproducer.common.database.H2Database
import org.amshove.kluent.`should be empty`
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InnboksQueriesTest {

    private val database = H2Database()
    private val innloggetBruker = InnloggetBrukerObjectMother.createInnloggetBruker()

    private val innboks1 = InnboksObjectMother.createInnboks(id = 1, eventId = "123", fodselsnummer = innloggetBruker.ident,
        aktiv = true, eksternVarsling = true, prefererteKanaler = listOf(PreferertKanal.SMS.toString()))
    private val innboks2 = InnboksObjectMother.createInnboks(id = 2, eventId = "345", fodselsnummer = innloggetBruker.ident,
        aktiv = true, eksternVarsling = true, prefererteKanaler = listOf(PreferertKanal.SMS.toString()))
    private val innboks3 = InnboksObjectMother.createInnboks(id = 3, eventId = "567", fodselsnummer = innloggetBruker.ident,
        aktiv = false, eksternVarsling = true, prefererteKanaler = listOf(PreferertKanal.EPOST.toString()))
    private val innboks4 = InnboksObjectMother.createInnboks(id = 4, eventId = "789", fodselsnummer = innloggetBruker.ident,
        aktiv = false, eksternVarsling = true, prefererteKanaler = listOf(PreferertKanal.EPOST.toString()))

    @BeforeAll
    fun `populer testdata`() {
        runBlocking {
            database.dbQuery { createInnboks(listOf(innboks1, innboks2, innboks3, innboks4)) }
        }
    }

    @AfterAll
    fun `slett testdata`() {
        runBlocking {
            database.dbQuery { deleteInnboks(listOf(innboks1, innboks2, innboks3, innboks4)) }
        }
    }

    @Test
    fun `Finn alle cachede Innboks-eventer for fodselsnummer`() {
        runBlocking {
            database.dbQuery { getAllInnboksByFodselsnummer(innloggetBruker) }.size `should be equal to` 4
        }
    }

    @Test
    fun `Finner kun aktive cachede Innboks-eventer for fodselsnummer`() {
        runBlocking {
            database.dbQuery { getAktivInnboksByFodselsnummer(innloggetBruker) }.size `should be equal to` 2
        }
    }

    @Test
    fun `Returnerer tom liste hvis Innboks-eventer for fodselsnummer ikke finnes`() {
        val brukerUtenEventer = InnloggetBrukerObjectMother.createInnloggetBruker("456")

        runBlocking {
            database.dbQuery { getAktivInnboksByFodselsnummer(brukerUtenEventer) }.`should be empty`()
        }
    }

    @Test
    fun `Returnerer tom liste hvis Innboks-eventer hvis tom fodselsnummer`() {
        val brukerUtenFodselsnummer = InnloggetBrukerObjectMother.createInnloggetBruker("")

        runBlocking {
            database.dbQuery { getAktivInnboksByFodselsnummer(brukerUtenFodselsnummer) }.`should be empty`()
        }
    }
}
