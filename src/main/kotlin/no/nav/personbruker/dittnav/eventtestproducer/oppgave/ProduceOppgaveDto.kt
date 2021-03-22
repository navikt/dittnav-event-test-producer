package no.nav.personbruker.dittnav.eventtestproducer.oppgave

import kotlinx.serialization.Serializable

@Serializable
class ProduceOppgaveDto(val tekst: String,
                        val link: String,
                        val grupperingsid: String,
                        val eksternVarsling: Boolean = false) {
    override fun toString(): String {
        return "ProduceOppgaveDto{tekst='$tekst', link='$link', grupperingsid='$grupperingsid', eksternVarsling='$eksternVarsling'}"

    }
}
