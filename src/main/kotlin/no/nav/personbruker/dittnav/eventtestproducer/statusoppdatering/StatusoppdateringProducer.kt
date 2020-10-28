package no.nav.personbruker.dittnav.eventtestproducer.statusoppdatering

import no.nav.brukernotifikasjon.schemas.Nokkel
import no.nav.brukernotifikasjon.schemas.Statusoppdatering
import no.nav.personbruker.dittnav.eventtestproducer.common.InnloggetBruker
import no.nav.personbruker.dittnav.eventtestproducer.common.createKeyForEvent
import no.nav.personbruker.dittnav.eventtestproducer.common.kafka.KafkaProducerWrapper
import org.slf4j.LoggerFactory
import java.time.Instant

class StatusoppdateringProducer(private val statusoppdateringKafkaProducer: KafkaProducerWrapper<Statusoppdatering>, private val systembruker: String) {

    private val log = LoggerFactory.getLogger(StatusoppdateringProducer::class.java)

    fun produceStatusoppdateringEventForIdent(innloggetBruker: InnloggetBruker, dto: ProduceStatusoppdateringDto) {
        try {
            val key = createKeyForEvent(systembruker)
            val event = createStatusoppdateringForIdent(innloggetBruker, dto)
            sendEventToKafka(key, event)
        } catch (e: Exception) {
            log.error("Det skjedde en feil ved produsering av et event for brukeren $innloggetBruker", e)
        }
    }

    fun sendEventToKafka(key: Nokkel, event: Statusoppdatering) {
        statusoppdateringKafkaProducer.sendEvent(key, event)
    }

    fun createStatusoppdateringForIdent(innloggetBruker: InnloggetBruker, dto: ProduceStatusoppdateringDto): Statusoppdatering {
        val nowInMs = Instant.now().toEpochMilli()
        val build = Statusoppdatering.newBuilder()
                .setFodselsnummer(innloggetBruker.ident)
                .setGrupperingsId(dto.grupperingsid)
                .setLink(dto.link)
                .setTidspunkt(nowInMs)
                .setSikkerhetsnivaa(innloggetBruker.innloggingsnivaa)
                .setStatusGlobal(dto.statusGlobal)
                .setStatusIntern(dto.statusIntern)
                .setSakstema(dto.sakstema)
        return build.build()
    }

}
