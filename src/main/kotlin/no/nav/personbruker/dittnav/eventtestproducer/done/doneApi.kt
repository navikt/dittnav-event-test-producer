package no.nav.personbruker.dittnav.eventtestproducer.done

import io.ktor.routing.*
import no.nav.personbruker.dittnav.eventtestproducer.config.innloggetBruker
import no.nav.personbruker.dittnav.eventtestproducer.config.respond

fun Route.doneApi(doneEventService: DoneEventService) {

    get("/produce/done/all") {
        respond {
            doneEventService.markAllBrukernotifikasjonerAsDone(innloggetBruker)
            "Done-eventer er produsert for alle for brukeren: $innloggetBruker sine brukernotifikasjoner."
        }
    }

}
