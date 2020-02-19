package no.nav.personbruker.dittnav.eventtestproducer.beskjed

import io.mockk.coEvery
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.eventtestproducer.common.InnloggetBrukerObjectMother
import no.nav.personbruker.dittnav.eventtestproducer.common.database.H2Database
import org.junit.jupiter.api.Test
import org.amshove.kluent.*

class BeskjedQueriesTest {

    private val database = H2Database()
    private val innloggetBruker = InnloggetBrukerObjectMother.createInnloggetBruker()

    @Test
    fun `Finn alle cachede Beskjed-eventer for fodselsnummer`() {
        val expectedIdent = "12345"

        coEvery { innloggetBruker.token.jwtTokenClaims.getStringClaim("sub") } returns expectedIdent

        runBlocking {
            database.dbQuery { getAllBeskjedByFodselsnummer(innloggetBruker) }.size `should be equal to` 3
        }
    }

    @Test
    fun `Finner kun aktive cachede Beskjed-eventer for fodselsnummer`() {
        val expectedIdent = "12345"

        coEvery { innloggetBruker.token.jwtTokenClaims.getStringClaim("sub") } returns expectedIdent

        runBlocking {
            database.dbQuery { getBeskjedByFodselsnummer(innloggetBruker) }.size `should be equal to` 2
        }
    }

    @Test
    fun `Returnerer tom liste hvis Beskjed-eventer for fodselsnummer ikke finnes`() {
        val expectedIdent = "dummyIdent"
        val subClaimThatIsNotAnIdent = "dummyClaimThatIsNotAnIdent"

        coEvery { innloggetBruker.token.jwtTokenClaims.getStringClaim("pid") } returns expectedIdent
        coEvery { innloggetBruker.token.jwtTokenClaims.getStringClaim("sub") } returns subClaimThatIsNotAnIdent

        runBlocking {
            database.dbQuery { getBeskjedByFodselsnummer(innloggetBruker) }.`should be empty`()
        }
    }


    @Test
    fun `Returnerer tom liste hvis Beskjed-eventer hvis tom fodselsnummer`() {
        val expectedIdent = ""
        val subClaimThatIsNotAnIdent = "dummyClaimThatIsNotAnIdent"

        coEvery { innloggetBruker.token.jwtTokenClaims.getStringClaim("pid") } returns expectedIdent
        coEvery { innloggetBruker.token.jwtTokenClaims.getStringClaim("sub") } returns subClaimThatIsNotAnIdent

        runBlocking {
            database.dbQuery { getBeskjedByFodselsnummer(innloggetBruker) }.`should be empty`()
        }
    }
}
