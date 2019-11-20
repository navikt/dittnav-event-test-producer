package no.nav.personbruker.dittnav.eventtestproducer.innboks

import io.ktor.routing.Route
import io.ktor.routing.post
import no.nav.personbruker.dittnav.eventtestproducer.config.respondForParameterType
import no.nav.personbruker.dittnav.eventtestproducer.config.userIdent

fun Route.innboksApi() {
    post("/produce/innboks") {
        respondForParameterType<ProduceInnboksDto> { innboksDto ->
            InnboksProducer.produceInnboksEventForIdent(userIdent, innboksDto)
            "Et innboks-event for identen: $userIdent har blitt lagt på kafka."
        }
    }
}