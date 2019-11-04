package no.nav.personbruker.dittnav.eventtestproducer.common.database

import java.time.ZonedDateTime

interface Brukernotifikasjon {
    val id: Int?
    val produsent: String
    val eventTidspunkt: ZonedDateTime
    val aktorId: String
    val eventId: String
    val dokumentId: String
    val sikkerhetsnivaa: Int
    val sistOppdatert: ZonedDateTime
    val aktiv: Boolean
    val tekst: String
    val link: String
}
