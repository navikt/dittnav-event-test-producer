package no.nav.personbruker.dittnav.eventtestproducer.informasjon

import io.ktor.routing.Route
import io.ktor.routing.post
import no.nav.personbruker.dittnav.eventtestproducer.config.respondForParameterType
import no.nav.personbruker.dittnav.eventtestproducer.config.userIdent

fun Route.informasjonApi() {

    post("/produce/informasjon") {
        respondForParameterType<ProduceInformasjonDto> { informasjonDto ->
            InformasjonProducer.produceInformasjonEventForIdent(userIdent, informasjonDto)
            "Et informasjons-event for identen: $userIdent har blitt lagt på kafka."
        }
    }

}
