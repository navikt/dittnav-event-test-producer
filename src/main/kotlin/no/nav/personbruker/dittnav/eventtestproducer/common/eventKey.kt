package no.nav.personbruker.dittnav.eventtestproducer.common

import de.huxhorn.sulky.ulid.ULID
import no.nav.brukernotifikasjon.schemas.Nokkel
import no.nav.brukernotifikasjon.schemas.builders.NokkelBuilder
import java.util.*

fun createKeyForEvent(systembruker: String): Nokkel {
    return NokkelBuilder()
            .withEventId(ULID().nextValue())
            .withSystembruker(systembruker)
            .build()
}

fun createKeyForEvent(eventId: String, systembruker: String): Nokkel {
    return NokkelBuilder()
            .withEventId(ULID.parseULID(eventId))
            .withSystembruker(systembruker)
            .build()
}
