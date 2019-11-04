package no.nav.personbruker.dittnav.eventtestproducer.informasjon

import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.request.receive
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.post
import no.nav.personbruker.dittnav.eventtestproducer.config.extractIdentFromToken

fun Route.informasjonApi() {

    post("/produce/informasjon") {
        val postParametersDto = call.receive<ProduceInformasjonDto>()
        val ident = extractIdentFromToken()
        InformasjonProducer.produceInformasjonEventForIdent(ident, postParametersDto)
        val msg = "Et informasjons-event for identen: $ident har blitt lagt på kafka."
        call.respondText(text = msg, contentType = ContentType.Text.Plain)
    }

}
