package no.nav.personbruker.dittnav.eventtestproducer.statusoppdatering

class ProduceStatusoppdateringDto (val link: String,
                                   val statusGlobal: String,
                                   val statusIntern: String,
                                   val sakstema: String)
{
    override fun toString(): String {
        return "ProduceStatusoppdateringDto{link='$link', statusGlobal='$statusGlobal', statusIntern='$statusIntern', sakstema='$sakstema'}"
    }
}