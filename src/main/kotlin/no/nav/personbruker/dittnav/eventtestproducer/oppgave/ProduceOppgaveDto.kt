package no.nav.personbruker.dittnav.eventtestproducer.oppgave

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
class ProduceOppgaveDto(val tekst: String,
                        val link: String,
                        val grupperingsid: String,
                        val eksternVarsling: Boolean = false,
                        val prefererteKanaler: List<String> = emptyList(),
                        val synligFremTil: LocalDateTime? = null,
                        val epostVarslingstekst: String? = null,
                        val smsVarslingstekst: String? = null) {
    override fun toString(): String {
        return "ProduceOppgaveDto{tekst='$tekst', link='$link', grupperingsid='$grupperingsid', eksternVarsling='$eksternVarsling', prefererteKanaler='$prefererteKanaler', synligFremTil='$synligFremTil', epostVarslingstekst='$epostVarslingstekst', smsVarslingstekst='$smsVarslingstekst'}"
    }
}
