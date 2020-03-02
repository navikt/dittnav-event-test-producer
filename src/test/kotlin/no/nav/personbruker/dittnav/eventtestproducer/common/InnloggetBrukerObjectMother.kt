package no.nav.personbruker.dittnav.eventtestproducer.common

import io.mockk.every
import io.mockk.mockk
import no.nav.security.token.support.core.jwt.JwtToken

object InnloggetBrukerObjectMother {

    fun createInnloggetBrukerMedInnloggingsnivaa4(): InnloggetBruker {
        val dummyJwtToken = mockk<JwtToken>()
        val dummyTokenAsString = "dummyToken"
        every { dummyJwtToken.tokenAsString } returns dummyTokenAsString
        every { dummyJwtToken.jwtTokenClaims.getStringClaim("sub") } returns "12345"
        every { dummyJwtToken.jwtTokenClaims.getStringClaim("acr") } returns "Level4"
        return InnloggetBruker(dummyJwtToken)
    }

    fun createInnloggetBrukerMedInnloggingsnivaa3(): InnloggetBruker {
        val dummyJwtToken = mockk<JwtToken>()
        val dummyTokenAsString = "dummyToken"
        every { dummyJwtToken.tokenAsString } returns dummyTokenAsString
        every { dummyJwtToken.jwtTokenClaims.getStringClaim("sub") } returns "12345"
        every { dummyJwtToken.jwtTokenClaims.getStringClaim("acr") } returns "Level3"
        return InnloggetBruker(dummyJwtToken)
    }

    fun createInnloggetBrukerWithSubject(subject: String): InnloggetBruker {
        val dummyJwtToken = mockk<JwtToken>()
        val dummyTokenAsString = "dummyToken"
        every { dummyJwtToken.tokenAsString } returns dummyTokenAsString
        every { dummyJwtToken.jwtTokenClaims.getStringClaim("sub") } returns subject
        return InnloggetBruker(dummyJwtToken)
    }

    fun createInnloggetBrukerUtenInnloggingsnivaa(): InnloggetBruker {
        val dummyJwtToken = mockk<JwtToken>()
        val dummyTokenAsString = "dummyToken"
        every { dummyJwtToken.tokenAsString } returns dummyTokenAsString
        every { dummyJwtToken.jwtTokenClaims.getStringClaim("sub") } returns "12345"
        return InnloggetBruker(dummyJwtToken)
    }

}
