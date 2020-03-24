package no.nav.personbruker.dittnav.eventtestproducer.oppgave

import io.ktor.routing.Route
import io.ktor.routing.post
import no.nav.personbruker.dittnav.eventtestproducer.config.innloggetBruker
import no.nav.personbruker.dittnav.eventtestproducer.config.respondForParameterType

fun Route.oppgaveApi(oppgaveProducer: OppgaveProducer) {

    post("/produce/oppgave") {
        respondForParameterType<ProduceOppgaveDto> { oppgaveDto ->
//            oppgaveProducer.produceOppgaveEventForIdent(innloggetBruker, oppgaveDto)
            "Midlertidig deaktivert grunnet ytelsestesting."
        }
    }

}
