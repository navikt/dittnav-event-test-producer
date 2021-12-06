package no.nav.personbruker.dittnav.eventtestproducer.beskjed

import kotlinx.datetime.*
import kotlinx.serialization.Serializable

@Serializable
class ProduceBeskjedDto(val tekst: String,
                        val link: String?,
                        val grupperingsid: String,
                        val eksternVarsling: Boolean = false,
                        val prefererteKanaler: List<String> = emptyList(),
                        val synligFremTil: LocalDateTime = Clock.System.now().plus(DateTimePeriod(days = 7), TimeZone.UTC).toLocalDateTime(TimeZone.UTC)) {
    override fun toString(): String {
        return "ProduceBeskjedDto{tekst='$tekst', link='$link', grupperingsid='$grupperingsid', eksternVarsling='$eksternVarsling', synligFremTil='$synligFremTil'}, prefererteKanaler='$prefererteKanaler"
    }
}
