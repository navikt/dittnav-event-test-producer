package no.nav.personbruker.dittnav.eventtestproducer.oppgave

import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.request.receive
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.post
import no.nav.personbruker.dittnav.eventtestproducer.config.userIdent

fun Route.oppgaveApi() {

    post("/produce/oppgave") {
        val postParametersDto = call.receive<ProduceOppgaveDto>()
        OppgaveProducer.produceOppgaveEventForIdent(userIdent, postParametersDto)
        val msg = "Et oppgave-event for identen: $userIdent har blitt lagt på kafka."
        call.respondText(text = msg, contentType = ContentType.Text.Plain)
    }

}
