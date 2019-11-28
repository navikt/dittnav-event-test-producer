package no.nav.personbruker.dittnav.eventtestproducer.beskjed

import io.ktor.routing.Route
import io.ktor.routing.post
import no.nav.personbruker.dittnav.eventtestproducer.config.respondForParameterType
import no.nav.personbruker.dittnav.eventtestproducer.config.userIdent

fun Route.beskjedApi() {

    post("/produce/informasjon") {
        respondForParameterType<ProduceBeskjedDto> { beskjedDto ->
            BeskjedProducer.produceBeskjedEventForIdent(userIdent, beskjedDto)
            "Et beskjed-event for identen: $userIdent har blitt lagt på kafka."
        }
    }

    post("/produce/beskjed") {
        respondForParameterType<ProduceBeskjedDto> { beskjedDto ->
            BeskjedProducer.produceBeskjedEventForIdent(userIdent, beskjedDto)
            "Et beskjed-event for identen: $userIdent har blitt lagt på kafka."
        }
    }

}
