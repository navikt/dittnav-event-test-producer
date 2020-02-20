package no.nav.personbruker.dittnav.eventtestproducer.common

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.auth.authentication
import io.ktor.util.pipeline.PipelineContext
import no.nav.security.token.support.core.jwt.JwtToken
import no.nav.security.token.support.ktor.OIDCValidationContextPrincipal

class InnloggetBruker(val token: JwtToken) {

    private val allowOnlyDigits = """\d*""".toRegex()

    fun generateAuthenticationHeader(): String {
        return "Bearer " + token.tokenAsString
    }

    fun getIdentFromToken(): String {
        var ident = token.jwtTokenClaims.getStringClaim("sub")

        if (isSubjectContainsOtherCharactersThanDigits(ident)) {
            ident = token.jwtTokenClaims.getStringClaim("pid")
        }
        return ident
    }

    private fun isSubjectContainsOtherCharactersThanDigits(ident: String): Boolean {
        return !allowOnlyDigits.matches(ident)
    }
}

val PipelineContext<Unit, ApplicationCall>.innloggetBruker: InnloggetBruker
    get() =
        call.authentication.principal<OIDCValidationContextPrincipal>()
                ?.context
                ?.firstValidToken
                ?.map { token -> InnloggetBruker(token) }
                ?.get()
                ?: throw Exception("Det ble ikke funnet noe token. Dette skal ikke kunne skje.")
