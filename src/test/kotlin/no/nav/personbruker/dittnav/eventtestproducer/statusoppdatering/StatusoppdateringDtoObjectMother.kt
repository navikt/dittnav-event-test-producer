package no.nav.personbruker.dittnav.eventtestproducer.statusoppdatering

object StatusoppdateringDtoObjectMother {

    fun createStatusoppdateringDto(
                                   link: String,
                                   statusGlobal: String,
                                   statusIntern: String,
                                   sakstema: String): ProduceStatusoppdateringDto {
        return ProduceStatusoppdateringDto(
                link = link,
                statusGlobal = statusGlobal,
                statusIntern = statusIntern,
                sakstema = sakstema)
    }
}
