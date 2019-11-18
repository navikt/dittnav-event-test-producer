package no.nav.personbruker.dittnav.eventtestproducer.innboks

import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.request.receive
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.post
import no.nav.personbruker.dittnav.eventtestproducer.config.userIdent

fun Route.innboksApi() {
    post("/produce/innboks") {
        val postParametersDto = call.receive<ProduceInnboksDto>()
        InnboksProducer.produceInnboksEventForIdent(userIdent, postParametersDto)
        val msg = "Et innboks-event for identen: $userIdent har blitt lagt på kafka."
        call.respondText(text = msg, contentType = ContentType.Text.Plain)
    }
}