package no.nav.personbruker.dittnav.eventtestproducer.done

import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.request.receive
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.post
import no.nav.personbruker.dittnav.eventtestproducer.config.userIdent

fun Route.doneApi(doneEventService: DoneEventService) {

    post("/produce/done/all") {
        doneEventService.markAllBrukernotifikasjonerAsDone(userIdent)
        val msg = "Done-eventer er produsert for alle identen: $userIdent sine brukernotifikasjoner."
        call.respondText(text = msg, contentType = ContentType.Text.Plain)
    }

    post("/produce/done") {
        val postParametersDto = call.receive<ProduceDoneDto>()
        doneEventService.markEventAsDone(userIdent, postParametersDto.eventId)
        val msg = "Done-event er produsert for identen: $userIdent sitt event med eventID: ${postParametersDto.eventId}."
        call.respondText(text = msg, contentType = ContentType.Text.Plain)
    }

}
