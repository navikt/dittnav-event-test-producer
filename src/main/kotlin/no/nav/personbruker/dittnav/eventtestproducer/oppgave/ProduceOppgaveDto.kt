package no.nav.personbruker.dittnav.eventtestproducer.oppgave

class ProduceOppgaveDto(val tekst: String, val link: String, val eksternVarsling: Boolean = false) {
    override fun toString(): String {
        return "ProduceOppgaveDto{tekst='$tekst', link='$link', eksternVarsling='$eksternVarsling'}"
    }
}
