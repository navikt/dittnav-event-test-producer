package no.nav.personbruker.dittnav.eventtestproducer.oppgave

class ProduceOppgaveDto(val tekst: String,
                        val link: String,
                        val grupperingsid: String) {
    override fun toString(): String {
        return "ProduceOppgaveDto{tekst='$tekst', link='$link', grupperingsid=$grupperingsid}"
    }
}
