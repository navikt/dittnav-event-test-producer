package no.nav.personbruker.dittnav.eventtestproducer.common

import no.nav.brukernotifikasjon.schemas.Nokkel
import java.util.*

fun createKeyForEvent(systemUserName: String): Nokkel {
    return Nokkel.newBuilder()
            .setEventId(UUID.randomUUID().toString())
            .setSystembruker(systemUserName)
            .build()
}

fun createKeyForEvent(eventId: String, systemUserName: String): Nokkel {
    return Nokkel.newBuilder()
            .setEventId(eventId)
            .setSystembruker(systemUserName)
            .build()
}
