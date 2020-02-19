package no.nav.personbruker.dittnav.eventtestproducer.oppgave

import io.ktor.routing.Route
import io.ktor.routing.post
import no.nav.personbruker.dittnav.eventtestproducer.config.respondForParameterType
import no.nav.personbruker.dittnav.eventtestproducer.common.innloggetBruker

fun Route.oppgaveApi() {

    post("/produce/oppgave") {
        respondForParameterType<ProduceOppgaveDto> { oppgaveDto ->
            OppgaveProducer.produceOppgaveEventForIdent(innloggetBruker, oppgaveDto)
            "Et oppgave-event for identen: ${innloggetBruker.getIdentFromToken()} har blitt lagt på kafka."
        }
    }

}
