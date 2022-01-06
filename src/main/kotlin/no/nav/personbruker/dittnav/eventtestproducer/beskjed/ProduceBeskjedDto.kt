package no.nav.personbruker.dittnav.eventtestproducer.beskjed

import kotlinx.datetime.*
import kotlinx.serialization.Serializable

@Serializable
class ProduceBeskjedDto(val tekst: String,
                        val link: String?,
                        val grupperingsid: String,
                        val eksternVarsling: Boolean = false,
                        val prefererteKanaler: List<String> = emptyList(),
                        val synligFremTil: LocalDateTime? = null,
                        val epostVarslingstekst: String? = null,
                        val smsVarslingstekst: String? = null) {
    override fun toString(): String {
        return "ProduceBeskjedDto{tekst='$tekst', link='$link', grupperingsid='$grupperingsid', eksternVarsling='$eksternVarsling', synligFremTil='$synligFremTil', prefererteKanaler='$prefererteKanaler', epostVarslingstekst='$epostVarslingstekst', smsVarslingstekst='$smsVarslingstekst'}"
    }
}
