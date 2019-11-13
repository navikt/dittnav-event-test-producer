package no.nav.personbruker.dittnav.eventtestproducer.informasjon

class ProduceInformasjonDto(val tekst: String, val link: String) {
    override fun toString(): String {
        return "ProduceInformasjonDto{tekst='$tekst', lenke='$link'}"
    }
}
