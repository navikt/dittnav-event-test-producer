package no.nav.personbruker.dittnav.eventtestproducer.done

import io.ktor.routing.Route
import io.ktor.routing.post
import no.nav.personbruker.dittnav.eventtestproducer.config.respond
import no.nav.personbruker.dittnav.eventtestproducer.config.respondForParameterType
import no.nav.personbruker.dittnav.eventtestproducer.config.userIdent

fun Route.doneApi(doneEventService: DoneEventService) {

    post("/produce/done/all") {
        respond {
            doneEventService.markAllBrukernotifikasjonerAsDone(userIdent)
            "Done-eventer er produsert for alle identen: $userIdent sine brukernotifikasjoner."
        }
    }

    post("/produce/done") {
        respondForParameterType<ProduceDoneDto> { doneDto ->
            doneEventService.markEventAsDone(userIdent, doneDto.eventId)
            "Done-event er produsert for identen: $userIdent sitt event med eventID: ${doneDto.eventId}."
        }
    }

}
