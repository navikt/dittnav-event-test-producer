package no.nav.personbruker.dittnav.eventtestproducer.innboks

import no.nav.personbruker.dittnav.eventtestproducer.common.database.Brukernotifikasjon
import java.time.ZonedDateTime

data class Innboks (
        override val id: Int,
        override val systembruker: String,
        override val eventTidspunkt: ZonedDateTime,
        override val fodselsnummer: String,
        override val eventId: String,
        override val grupperingsId: String,
        override val tekst: String,
        override val link: String,
        override val sikkerhetsnivaa: Int,
        override val sistOppdatert: ZonedDateTime,
        override val aktiv: Boolean,
        val eksternVarsling: Boolean,
        val prefererteKanaler: List<String>
) : Brukernotifikasjon {

    override fun toString(): String {
        return "Innboks(" +
                "id=$id, " +
                "systembruker=***, " +
                "eventTidspunkt=$eventTidspunkt, " +
                "fodselsnummer=***, " +
                "eventId=$eventId, " +
                "grupperingsId=$grupperingsId, " +
                "tekst=***, " +
                "link=***, " +
                "sikkerhetsnivaa=$sikkerhetsnivaa, " +
                "sistOppdatert=$sistOppdatert, " +
                "aktiv=$aktiv, " +
                "eksternVarsling=$eksternVarsling, " +
                "prefererteKanaler=$prefererteKanaler"
    }
}
