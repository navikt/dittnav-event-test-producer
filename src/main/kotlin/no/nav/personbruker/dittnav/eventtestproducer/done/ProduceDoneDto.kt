package no.nav.personbruker.dittnav.eventtestproducer.done

import kotlinx.serialization.Serializable

@Serializable
class ProduceDoneDto(val eventId: String) {
    override fun toString(): String {
        return "ProduceDoneDto{eventId='$eventId'}"
    }

}
