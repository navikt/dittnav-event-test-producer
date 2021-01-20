package no.nav.personbruker.dittnav.eventtestproducer.beskjed


class ProduceBeskjedDto(val tekst: String,
                        val link: String?,
                        val grupperingsid: String,
                        val eksternVarsling: Boolean = false) {
    override fun toString(): String {
        return "ProduceBeskjedDto{tekst='$tekst', link='$link', grupperingsid='$grupperingsid', eksternVarsling='$eksternVarsling'}"

    }
}
