package no.nav.personbruker.dittnav.eventtestproducer.innboks

import io.ktor.routing.Route
import io.ktor.routing.post
import no.nav.personbruker.dittnav.eventtestproducer.config.respondForParameterType
import no.nav.personbruker.dittnav.eventtestproducer.common.innloggetBruker

fun Route.innboksApi() {
    post("/produce/innboks") {
        respondForParameterType<ProduceInnboksDto> { innboksDto ->
            InnboksProducer.produceInnboksEventForIdent(innloggetBruker, innboksDto)
            "Et innboks-event for identen: ${innloggetBruker.getIdentFromToken()} har blitt lagt på kafka."
        }
    }
}