package no.nav.personbruker.dittnav.eventtestproducer.statusOppdatering

import io.ktor.routing.Route
import io.ktor.routing.post
import no.nav.personbruker.dittnav.eventtestproducer.config.innloggetBruker
import no.nav.personbruker.dittnav.eventtestproducer.config.respondForParameterType

fun Route.statusOppdateringApi(statusOppdateringProducer: StatusOppdateringProducer) {

    post("/produce/statusOppdatering") {
        respondForParameterType<ProduceStatusOppdateringDto> { statusOppdateringDto ->
            statusOppdateringProducer.produceStatusOppdateringEventForIdent(innloggetBruker, statusOppdateringDto)
            "Et statusOppdatering-event for brukeren: $innloggetBruker har blitt lagt på kafka."
        }
    }

}