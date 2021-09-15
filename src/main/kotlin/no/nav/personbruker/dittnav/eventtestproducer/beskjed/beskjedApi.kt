package no.nav.personbruker.dittnav.eventtestproducer.beskjed

import io.ktor.routing.Route
import io.ktor.routing.post
import no.nav.personbruker.dittnav.eventtestproducer.config.innloggetBruker
import no.nav.personbruker.dittnav.eventtestproducer.config.respondForParameterType

fun Route.beskjedApi(beskjedProducer: BeskjedProducer) {

    post("/produce/beskjed") {
        this.respondForParameterType<ProduceBeskjedDto> { beskjedDto ->
            beskjedProducer.produceBeskjedEventForIdent(innloggetBruker, beskjedDto)
            "Et beskjed-event for brukeren: $innloggetBruker har blitt lagt på kafka."
        }
    }

}
