package no.nav.personbruker.dittnav.eventtestproducer.beskjed

import io.ktor.routing.Route
import io.ktor.routing.post
import no.nav.personbruker.dittnav.eventtestproducer.config.respondForParameterType

fun Route.beskjedApi(beskjedProducer: BeskjedProducer) {

    post("/produce/informasjon") {
        respondForParameterType<ProduceBeskjedDto> { beskjedDto ->
            "Denne operasjonen er deprecated, bruker heller /produce/beskjed"
        }
    }

    post("/produce/beskjed") {
        respondForParameterType<ProduceBeskjedDto> { beskjedDto ->
//            beskjedProducer.produceBeskjedEventForIdent(innloggetBruker, beskjedDto)
            "Midlertidig deaktivert grunnet ytelsestesting."
        }
    }

}
