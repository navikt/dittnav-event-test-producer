package no.nav.personbruker.dittnav.eventtestproducer.statusoppdatering

import io.ktor.routing.Route
import io.ktor.routing.post
import no.nav.personbruker.dittnav.eventtestproducer.config.innloggetBruker
import no.nav.personbruker.dittnav.eventtestproducer.config.respondForParameterType

fun Route.statusoppdateringApi(statusoppdateringProducer: StatusoppdateringProducer) {

    post("/produce/statusoppdatering") {
        respondForParameterType<ProduceStatusoppdateringDto> { statusoppdateringDto ->
            statusoppdateringProducer.produceStatusoppdateringEventForIdent(innloggetBruker, statusoppdateringDto)
            "Et statusoppdatering-event for brukeren: $innloggetBruker har blitt lagt på kafka."
        }
    }

}