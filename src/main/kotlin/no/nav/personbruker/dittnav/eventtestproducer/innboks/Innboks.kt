package no.nav.personbruker.dittnav.eventtestproducer.innboks

import no.nav.personbruker.dittnav.eventtestproducer.common.database.Brukernotifikasjon
import java.time.ZonedDateTime

data class Innboks (
        override val id: Int,
        override val produsent: String,
        override val eventTidspunkt: ZonedDateTime,
        override val aktorId: String,
        override val eventId: String,
        override val dokumentId: String,
        override val tekst: String,
        override val link: String,
        override val sikkerhetsnivaa: Int,
        override val sistOppdatert: ZonedDateTime,
        override val aktiv: Boolean
) : Brukernotifikasjon