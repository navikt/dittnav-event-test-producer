package no.nav.personbruker.dittnav.eventtestproducer.oppgave

import io.mockk.coEvery
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.eventtestproducer.common.InnloggetBrukerObjectMother
import no.nav.personbruker.dittnav.eventtestproducer.common.database.H2Database
import org.junit.jupiter.api.Test
import org.amshove.kluent.*

class OppgaveQueriesTest {

    private val database = H2Database()
    private val innloggetBruker = InnloggetBrukerObjectMother.createInnloggetBruker()

    @Test
    fun `Finn alle cachede Oppgave-eventer for fodselsnummer`() {
        val expectedIdent = "12345"

        coEvery { innloggetBruker.token.jwtTokenClaims.getStringClaim("sub") } returns expectedIdent

        runBlocking {
            database.dbQuery { getAllOppgaveByFodselsnummer(innloggetBruker) }.size `should be equal to` 3
        }
    }

    @Test
    fun `Finn alle aktive cachede Oppgave-eventer for fodselsnummer`() {
        val expectedIdent = "12345"

        coEvery { innloggetBruker.token.jwtTokenClaims.getStringClaim("sub") } returns expectedIdent

        runBlocking {
            database.dbQuery { getOppgaveByFodselsnummer(innloggetBruker) }.size `should be equal to` 2
        }
    }

    @Test
    fun `Returnerer tom liste hvis Oppgave-eventer for fodselsnummer ikke finnes`() {
        val expectedIdent = "dummyIdent"
        val subClaimThatIsNotAnIdent = "dummyClaimThatIsNotAnIdent"

        coEvery { innloggetBruker.token.jwtTokenClaims.getStringClaim("pid") } returns expectedIdent
        coEvery { innloggetBruker.token.jwtTokenClaims.getStringClaim("sub") } returns subClaimThatIsNotAnIdent

        runBlocking {
            database.dbQuery { getOppgaveByFodselsnummer(innloggetBruker) }.isEmpty()
        }
    }

    @Test
    fun `Returnerer tom liste hvis Oppgave-eventer hvis tom fodselsnummer`() {
        val expectedIdent = ""
        val subClaimThatIsNotAnIdent = "dummyClaimThatIsNotAnIdent"

        coEvery { innloggetBruker.token.jwtTokenClaims.getStringClaim("pid") } returns expectedIdent
        coEvery { innloggetBruker.token.jwtTokenClaims.getStringClaim("sub") } returns subClaimThatIsNotAnIdent

        runBlocking {
            database.dbQuery { getOppgaveByFodselsnummer(innloggetBruker) }.isEmpty()
        }
    }
}
