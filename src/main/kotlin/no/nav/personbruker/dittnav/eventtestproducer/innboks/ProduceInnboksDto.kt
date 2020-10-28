package no.nav.personbruker.dittnav.eventtestproducer.innboks

class ProduceInnboksDto(val tekst: String,
                        val link: String,
                        val grupperingsid: String) {
    override fun toString(): String {
        return "ProduceInnboksDto{tekst='$tekst', link='$link', grupperingsid='$grupperingsid'}"
    }
}
