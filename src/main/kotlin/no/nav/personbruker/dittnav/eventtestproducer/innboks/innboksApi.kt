package no.nav.personbruker.dittnav.eventtestproducer.innboks

import io.ktor.routing.Route
import io.ktor.routing.post
import no.nav.personbruker.dittnav.eventtestproducer.config.innloggetBruker
import no.nav.personbruker.dittnav.eventtestproducer.config.respondForParameterType

fun Route.innboksApi(innboksProducer: InnboksProducer) {
    post("/produce/innboks") {
        respondForParameterType<ProduceInnboksDto> { innboksDto ->
            innboksProducer.produceInnboksEventForIdent(innloggetBruker, innboksDto)
            "Et innboks-event for for brukeren: $innloggetBruker har blitt lagt på kafka."
        }
    }
}
