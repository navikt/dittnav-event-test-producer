package no.nav.personbruker.dittnav.eventtestproducer.statusoppdatering

import no.nav.brukernotifikasjon.schemas.Nokkel
import no.nav.brukernotifikasjon.schemas.Statusoppdatering
import no.nav.brukernotifikasjon.schemas.builders.StatusoppdateringBuilder
import no.nav.brukernotifikasjon.schemas.builders.domain.StatusGlobal
import no.nav.personbruker.dittnav.eventtestproducer.common.kafka.KafkaProducerWrapper
import no.nav.personbruker.dittnav.eventtestproducer.common.InnloggetBruker
import no.nav.personbruker.dittnav.eventtestproducer.common.createKeyForEvent
import org.slf4j.LoggerFactory
import java.net.URL
import java.time.LocalDateTime

class StatusoppdateringProducer(private val statusoppdateringKafkaProducer: KafkaProducerWrapper<Nokkel, Statusoppdatering>, private val systembruker: String) {

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
        val now = LocalDateTime.now()
        val build = StatusoppdateringBuilder()
                .withFodselsnummer(innloggetBruker.ident)
                .withGrupperingsId(dto.grupperingsid)
                .withLink(URL(dto.link))
                .withTidspunkt(now)
                .withSikkerhetsnivaa(innloggetBruker.innloggingsnivaa)
                .withStatusGlobal(StatusGlobal.valueOf(dto.statusGlobal))
                .withStatusIntern(dto.statusIntern)
                .withSakstema(dto.sakstema)
        return build.build()
    }

}
