package no.nav.personbruker.dittnav.eventtestproducer.statusOppdatering

class ProduceStatusOppdateringDto (val link: String,
                                   val statusGlobal: String,
                                   val statusIntern: String,
                                   val sakstema: String)
{
    override fun toString(): String {
        return "ProduceStatusOppdateringDto{link='$link', statusGlobal='$statusGlobal', statusIntern='$statusIntern', sakstema='$sakstema'}"
    }
}