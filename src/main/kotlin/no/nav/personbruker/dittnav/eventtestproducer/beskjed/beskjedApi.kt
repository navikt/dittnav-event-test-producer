package no.nav.personbruker.dittnav.eventtestproducer.beskjed

import io.ktor.routing.Route
import io.ktor.routing.post
import no.nav.personbruker.dittnav.eventtestproducer.config.respondForParameterType
import no.nav.personbruker.dittnav.eventtestproducer.common.innloggetBruker

fun Route.beskjedApi() {

    post("/produce/informasjon") {
        respondForParameterType<ProduceBeskjedDto> { beskjedDto ->
            BeskjedProducer.produceBeskjedEventForIdent(innloggetBruker, beskjedDto)
            "Et beskjed-event for identen: ${innloggetBruker.getIdent()} har blitt lagt på kafka."
        }
    }

    post("/produce/beskjed") {
        respondForParameterType<ProduceBeskjedDto> { beskjedDto ->
            BeskjedProducer.produceBeskjedEventForIdent(innloggetBruker, beskjedDto)
            "Et beskjed-event for identen: ${innloggetBruker.getIdent()} har blitt lagt på kafka."
        }
    }

}
