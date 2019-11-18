package no.nav.personbruker.dittnav.eventtestproducer.config

import com.auth0.jwt.JWT
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.request.receive
import io.ktor.response.respondText
import io.ktor.util.pipeline.PipelineContext

val PipelineContext<Unit, ApplicationCall>.userIdent get() = extractIdentFromToken()

private fun PipelineContext<Unit, ApplicationCall>.extractIdentFromToken(): String {
    var authToken = getTokenFromHeader()
    if (authToken == null) {
        authToken = getTokenFromCookie()
    }
    verifyThatATokenWasFound(authToken)
    return extractSubject(authToken)
}

private fun verifyThatATokenWasFound(authToken: String?) {
    if (authToken == null) {
        throw Exception("Token ble ikke funnet. Dette skal ikke kunne skje.")
    }
}

suspend inline fun PipelineContext<Unit, ApplicationCall>.respond(handler: () -> String) {
    val message = handler.invoke()
    call.respondText(text = message, contentType = ContentType.Text.Plain)
}

suspend inline fun <reified T : Any> PipelineContext<Unit, ApplicationCall>.respondForParameterType(handler:(T) -> String) {
    val postParametersDto: T = call.receive()
    val message = handler.invoke(postParametersDto)
    call.respondText(text = message, contentType = ContentType.Text.Plain)
}

private fun PipelineContext<Unit, ApplicationCall>.getTokenFromHeader() =
        call.request.headers[HttpHeaders.Authorization]?.replace("Bearer ", "")

private fun PipelineContext<Unit, ApplicationCall>.getTokenFromCookie() =
        call.request.cookies["selvbetjening-idtoken"]

private fun extractSubject(authToken: String?): String {
    val jwt = JWT.decode(authToken)
    return jwt.getClaim("sub").asString() ?: "subject (ident) ikke funnet"
}
