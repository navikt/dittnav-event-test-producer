import no.nav.personbruker.dittnav.eventtestproducer.common.database.Brukernotifikasjon
import java.time.ZonedDateTime

data class Beskjed(
        override val aktiv: Boolean,
        override val fodselsnummer: String,
        override val grupperingsId: String,
        override val eventId: String,
        override val eventTidspunkt: ZonedDateTime,
        override val id: Int?,
        override val systembruker: String,
        override val sikkerhetsnivaa: Int,
        override val sistOppdatert: ZonedDateTime,
        override val tekst: String,
        override val link: String
) : Brukernotifikasjon {

    override fun toString(): String {
        return "Beskjed(" +
                "id=$id, " +
                "systembruker=***, " +
                "eventId=$eventId, " +
                "eventTidspunkt=$eventTidspunkt, " +
                "fodselsnummer=***, " +
                "grupperingsId=$grupperingsId, " +
                "tekst=***, " +
                "link=***, " +
                "sikkerhetsnivaa=$sikkerhetsnivaa, " +
                "sistOppdatert=$sistOppdatert, " +
                "aktiv=$aktiv"
    }
}
