package no.nav.personbruker.dittnav.eventtestproducer.beskjed

import kotlinx.serialization.Serializable

@Serializable
class ProduceBeskjedDto(val tekst: String,
                        val link: String?,
                        val grupperingsid: String,
                        val eksternVarsling: Boolean = false,
                        val prefererteKanaler: List<String> = emptyList()) {
    override fun toString(): String {
        return "ProduceBeskjedDto{tekst='$tekst', link='$link', grupperingsid='$grupperingsid', eksternVarsling='$eksternVarsling'}, prefererteKanaler='$prefererteKanaler"
    }
}
