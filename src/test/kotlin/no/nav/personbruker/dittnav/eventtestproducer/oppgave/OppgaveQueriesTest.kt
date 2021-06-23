package no.nav.personbruker.dittnav.eventtestproducer.oppgave

import kotlinx.coroutines.runBlocking
import no.nav.brukernotifikasjon.schemas.builders.domain.PreferertKanal
import no.nav.personbruker.dittnav.eventtestproducer.common.InnloggetBrukerObjectMother
import no.nav.personbruker.dittnav.eventtestproducer.common.database.H2Database
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OppgaveQueriesTest {

    private val database = H2Database()
    private val innloggetBruker = InnloggetBrukerObjectMother.createInnloggetBruker()

    private val oppgave1 = OppgaveObjectMother.createOppgave(id = 1, eventId = "123", fodselsnummer = innloggetBruker.ident, aktiv = true, eksternVarsling = true, prefererteKanaler = listOf(PreferertKanal.SMS.toString()))
    private val oppgave2 = OppgaveObjectMother.createOppgave(id = 2, eventId = "345", fodselsnummer = innloggetBruker.ident, aktiv = true, eksternVarsling = true, prefererteKanaler = listOf(PreferertKanal.SMS.toString()))
    private val oppgave3 = OppgaveObjectMother.createOppgave(id = 3, eventId = "567", fodselsnummer = innloggetBruker.ident, aktiv = false, eksternVarsling = true, prefererteKanaler = listOf(PreferertKanal.EPOST.toString()))
    private val oppgave4 = OppgaveObjectMother.createOppgave(id = 4, eventId = "789", fodselsnummer = "54321", aktiv = true, eksternVarsling = true, prefererteKanaler = listOf(PreferertKanal.EPOST.toString()))

    @BeforeAll
    fun `populer testdata`() {
        runBlocking {
            database.dbQuery { createOppgave(listOf(oppgave1, oppgave2, oppgave3, oppgave4)) }
        }
    }

    @AfterAll
    fun `slett testdata`() {
        runBlocking {
            database.dbQuery { deleteOppgave(listOf(oppgave1, oppgave2, oppgave3, oppgave4)) }
        }
    }

    @Test
    fun `Finn alle cachede Oppgave-eventer for fodselsnummer`() {
        runBlocking {
            database.dbQuery { getAllOppgaveByFodselsnummer(innloggetBruker) }.size `should be equal to` 3
        }
    }

    @Test
    fun `Finn alle aktive cachede Oppgave-eventer for fodselsnummer`() {
        runBlocking {
            database.dbQuery { getAktivOppgaveByFodselsnummer(innloggetBruker) }.size `should be equal to` 2
        }
    }

    @Test
    fun `Returnerer tom liste hvis Oppgave-eventer for fodselsnummer ikke finnes`() {
        val bruker = InnloggetBrukerObjectMother.createInnloggetBruker("456")

        runBlocking {
            database.dbQuery { getAktivOppgaveByFodselsnummer(bruker) }.isEmpty()
        }
    }

    @Test
    fun `Returnerer tom liste hvis Oppgave-eventer hvis tom fodselsnummer`() {
        val brukerUtenFodselsnummer = InnloggetBrukerObjectMother.createInnloggetBruker("")

        runBlocking {
            database.dbQuery { getAktivOppgaveByFodselsnummer(brukerUtenFodselsnummer) }.isEmpty()
        }
    }
}
