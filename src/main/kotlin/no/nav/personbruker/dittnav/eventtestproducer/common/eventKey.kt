package no.nav.personbruker.dittnav.eventtestproducer.common

import no.nav.brukernotifikasjon.schemas.Nokkel
import java.util.*

fun createKeyForEvent(systembruker: String): Nokkel {
    return Nokkel.newBuilder()
            .setEventId(UUID.randomUUID().toString())
            .setSystembruker(systembruker)
            .build()
}

fun createKeyForEvent(eventId: String, systembruker: String): Nokkel {
    return Nokkel.newBuilder()
            .setEventId(eventId)
            .setSystembruker(systembruker)
            .build()
}
