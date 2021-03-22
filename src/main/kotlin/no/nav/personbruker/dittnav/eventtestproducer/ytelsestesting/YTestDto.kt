package no.nav.personbruker.dittnav.eventtestproducer.ytelsestesting

import kotlinx.serialization.Serializable

@Serializable
data class YTestDto(val antallEventer: Int = 50000,
                    val ident: String = "12345678901",
                    val eksternVarsling: Boolean = false) {

    override fun toString(): String {
        return "ProduceBeskjedDto{antallEventer='$antallEventer', ident='$ident', eksternVarsling='$eksternVarsling'}"

    }
}
