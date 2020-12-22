package no.nav.personbruker.dittnav.eventtestproducer.common

import no.nav.brukernotifikasjon.schemas.Nokkel
import no.nav.brukernotifikasjon.schemas.builders.NokkelBuilder
import java.util.*

fun createKeyForEvent(systembruker: String): Nokkel {
    return NokkelBuilder()
            .withEventId(UUID.randomUUID().toString())
            .withSystembruker(systembruker)
            .build()
}

fun createKeyForEvent(eventId: String, systembruker: String): Nokkel {
    return NokkelBuilder()
            .withEventId(eventId)
            .withSystembruker(systembruker)
            .build()
}
