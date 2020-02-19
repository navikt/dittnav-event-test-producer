package no.nav.personbruker.dittnav.eventtestproducer.done

import io.ktor.routing.Route
import io.ktor.routing.post
import no.nav.personbruker.dittnav.eventtestproducer.config.respond
import no.nav.personbruker.dittnav.eventtestproducer.config.respondForParameterType
import no.nav.personbruker.dittnav.eventtestproducer.common.innloggetBruker

fun Route.doneApi(doneEventService: DoneEventService) {

    post("/produce/done/all") {
        respond {
            doneEventService.markAllBrukernotifikasjonerAsDone(innloggetBruker)
            "Done-eventer er produsert for alle identen: ${innloggetBruker.getIdentFromToken()} sine brukernotifikasjoner."
        }
    }

    post("/produce/done") {
        respondForParameterType<ProduceDoneDto> { doneDto ->
            doneEventService.markEventAsDone(innloggetBruker, doneDto.eventId)
            "Done-event er produsert for identen: ${innloggetBruker.getIdentFromToken()} sitt event med eventID: ${doneDto.eventId}."
        }
    }

}
