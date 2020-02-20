package no.nav.personbruker.dittnav.eventtestproducer.common

import io.mockk.coEvery
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test

internal class InnloggetBrukerTest {

    private val innloggetBruker = InnloggetBrukerObjectMother.createInnloggetBruker()

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
}
