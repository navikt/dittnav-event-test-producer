package no.nav.personbruker.dittnav.eventtestproducer.oppgave

class ProduceOppgaveDto(val tekst: String, val link: String) {
    override fun toString(): String {
        return "ProduceDto{tekst='$tekst', lenke='$link'}"
    }
}
