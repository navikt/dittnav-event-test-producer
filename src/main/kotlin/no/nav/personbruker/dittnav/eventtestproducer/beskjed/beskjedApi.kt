package no.nav.personbruker.dittnav.eventtestproducer.beskjed

import io.ktor.routing.Route
import io.ktor.routing.post
import no.nav.personbruker.dittnav.eventtestproducer.config.respondForParameterType
import no.nav.personbruker.dittnav.eventtestproducer.config.innloggetBruker

fun Route.beskjedApi() {

    post("/produce/beskjed") {
        respondForParameterType<ProduceBeskjedDto> { beskjedDto ->
            BeskjedProducer.produceBeskjedEventForIdent(innloggetBruker, beskjedDto)
            "Et beskjed-event for brukeren: $innloggetBruker har blitt lagt på kafka."
        }
    }

}
