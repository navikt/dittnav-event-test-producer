package no.nav.personbruker.dittnav.eventtestproducer.common

import io.mockk.coEvery
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should equal`
import org.amshove.kluent.`should throw`
import org.amshove.kluent.invoking
import org.junit.jupiter.api.Test

internal class InnloggetBrukerTest {

    private val innloggetBruker = InnloggetBrukerObjectMother.createInnloggetBrukerMedInnloggingsnivaa4()

    @Test
    fun `should return string with Bearer token`() {
        val expectedToken = "Bearer dummyToken"

        runBlocking {
            val actualToken = innloggetBruker.generateAuthenticationHeader()
            actualToken `should be equal to` expectedToken
        }
    }

    @Test
    fun `should return string with ident from pid token claim`() {
        val expectedIdent = "dummyIdent"
        val subClaimThatIsNotAnIdent = "dummyClaimThatIsNotAnIdent"

        coEvery { innloggetBruker.token.jwtTokenClaims.getStringClaim("pid") } returns expectedIdent
        coEvery { innloggetBruker.token.jwtTokenClaims.getStringClaim("sub") } returns subClaimThatIsNotAnIdent

        runBlocking {
            val actualIdent = innloggetBruker.getIdent()
            actualIdent `should be equal to` expectedIdent
        }
    }

    @Test
    fun `should return string with ident from sub token claim`() {
        val expectedIdent = "123"
        val pidClaimThatIsNotAnIdent = "dummyClaimThatIsNotAnIdent"

        coEvery { innloggetBruker.token.jwtTokenClaims.getStringClaim("pid") } returns pidClaimThatIsNotAnIdent
        coEvery { innloggetBruker.token.jwtTokenClaims.getStringClaim("sub") } returns expectedIdent

        runBlocking {
            val actualIdent = innloggetBruker.getIdent()
            actualIdent `should be equal to` expectedIdent
        }
    }

    @Test
    fun `should return innloggingsnivaa for nivaa 4`() {
        val brukerPaaNivaa4 = InnloggetBrukerObjectMother.createInnloggetBrukerMedInnloggingsnivaa4()
        brukerPaaNivaa4.getInnloggingsnivaa() `should equal` 4
    }

    @Test
    fun `should return innloggingsnivaa for nivaa 3`() {
        val brukerPaaNivaa4 = InnloggetBrukerObjectMother.createInnloggetBrukerMedInnloggingsnivaa3()
        brukerPaaNivaa4.getInnloggingsnivaa() `should equal` 3
    }

    @Test
    fun `should throw error if innloggingsnivaa not present`() {
        val brukerUtenInnloggingsnivaa = InnloggetBrukerObjectMother.createInnloggetBrukerUtenInnloggingsnivaa()
        invoking {
            brukerUtenInnloggingsnivaa.getInnloggingsnivaa()
        } `should throw` Exception::class
    }

}
