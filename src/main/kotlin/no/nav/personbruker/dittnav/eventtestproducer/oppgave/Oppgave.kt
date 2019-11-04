package no.nav.personbruker.dittnav.eventtestproducer.oppgave

import no.nav.personbruker.dittnav.eventtestproducer.common.database.Brukernotifikasjon
import java.time.ZonedDateTime

data class Oppgave(
        override val aktiv: Boolean,
        override val aktorId: String,
        override val dokumentId: String,
        override val eventId: String,
        override val eventTidspunkt: ZonedDateTime,
        override val id: Int?,
        override val produsent: String,
        override val sikkerhetsnivaa: Int,
        override val sistOppdatert: ZonedDateTime,
        override val tekst: String,
        override val link: String
) : Brukernotifikasjon
