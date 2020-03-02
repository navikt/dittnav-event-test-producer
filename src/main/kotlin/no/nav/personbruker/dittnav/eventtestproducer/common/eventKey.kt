package no.nav.personbruker.dittnav.eventtestproducer.common

import no.nav.brukernotifikasjon.schemas.Nokkel
import java.time.Instant

fun createKeyForEvent(systemUserName: String): Nokkel {
    val nowInMs = Instant.now().toEpochMilli()

    return Nokkel.newBuilder()
            .setEventId("$nowInMs")
            .setSystembruker(systemUserName)
            .build()
}

fun createKeyForEvent(eventId: String, systemUserName: String): Nokkel {
    return Nokkel.newBuilder()
            .setEventId(eventId)
            .setSystembruker(systemUserName)
            .build()
}
