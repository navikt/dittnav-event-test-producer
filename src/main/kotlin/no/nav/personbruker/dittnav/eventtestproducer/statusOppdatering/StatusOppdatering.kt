package no.nav.personbruker.dittnav.eventtestproducer.statusOppdatering

import java.time.ZonedDateTime

data class StatusOppdatering(
        val id: Int,
        val systembruker: String,
        val eventId: String,
        val eventTidspunkt: ZonedDateTime,
        val fodselsnummer: String,
        val grupperingsId: String,
        val link: String,
        val sikkerhetsnivaa: Int,
        val sistOppdatert: ZonedDateTime,
        val statusGlobal: String,
        val statusIntern: String?,
        val sakstema: String
) {
    override fun toString(): String {
        return "StatusOppdatering(" +
                "id=$id, " +
                "systembruker=***, " +
                "eventId=$eventId, " +
                "eventTidspunkt=$eventTidspunkt, " +
                "fodselsnummer=***, " +
                "grupperingsId=$grupperingsId, " +
                "link=***, " +
                "sikkerhetsnivaa=$sikkerhetsnivaa, " +
                "sistOppdatert=$sistOppdatert, " +
                "statusGlobal=$statusGlobal, " +
                "statusIntern=$statusIntern, " +
                "sakstema=$sakstema"
    }
}
