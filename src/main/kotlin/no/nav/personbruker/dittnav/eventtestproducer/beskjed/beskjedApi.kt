package no.nav.personbruker.dittnav.eventtestproducer.beskjed

import io.ktor.application.call
import io.ktor.routing.Route
import io.ktor.routing.post
import no.nav.personbruker.dittnav.eventtestproducer.config.innloggetBruker
import no.nav.personbruker.dittnav.eventtestproducer.config.respondForParameterType

fun Route.beskjedApi(beskjedProducer: BeskjedProducer) {

    post("/produce/beskjed") {
        respondForParameterType<ProduceBeskjedDto> { beskjedDto ->
            beskjedProducer.produceBeskjedEventForIdent(innloggetBruker, beskjedDto)
            "Et beskjed-event for brukeren: $innloggetBruker har blitt lagt på kafka."
        }
    }

    post("/produce/multiple/beskjed/{number}") {
        val number = call.parameters["number"]!!.toInt()
        respondForParameterType<ProduceBeskjedDto> { beskjedDto ->
            var counter = 0
            (0 until number).forEach {
                counter++
                beskjedProducer.produceBeskjedEventForIdent(innloggetBruker, beskjedDto)
            }
            "$counter beskjed-eventer for brukeren: $innloggetBruker har blitt lagt på kafka."
        }
    }

}
