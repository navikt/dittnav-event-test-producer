package no.nav.personbruker.dittnav.eventtestproducer.oppgave

import io.ktor.routing.Route
import io.ktor.routing.post
import no.nav.personbruker.dittnav.eventtestproducer.config.respondForParameterType
import no.nav.personbruker.dittnav.eventtestproducer.config.userIdent

fun Route.oppgaveApi() {

    post("/produce/oppgave") {
        respondForParameterType<ProduceOppgaveDto> { oppgaveDto ->
            OppgaveProducer.produceOppgaveEventForIdent(userIdent, oppgaveDto)
            "Et oppgave-event for identen: $userIdent har blitt lagt på kafka."
        }
    }

}
