package no.nav.personbruker.dittnav.eventtestproducer.statusoppdatering

import no.nav.brukernotifikasjon.schemas.builders.StatusoppdateringInputBuilder
import no.nav.brukernotifikasjon.schemas.builders.domain.StatusGlobal
import no.nav.brukernotifikasjon.schemas.input.NokkelInput
import no.nav.brukernotifikasjon.schemas.input.StatusoppdateringInput
import no.nav.personbruker.dittnav.eventtestproducer.common.InnloggetBruker
import no.nav.personbruker.dittnav.eventtestproducer.common.createKeyForEvent
import no.nav.personbruker.dittnav.eventtestproducer.common.kafka.KafkaProducerWrapper
import org.slf4j.LoggerFactory
import java.net.URL
import java.time.LocalDateTime
import java.time.ZoneOffset

class StatusoppdateringProducer(private val statusoppdateringKafkaProducer: KafkaProducerWrapper<NokkelInput, StatusoppdateringInput>, private val systembruker: String) {

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

    fun sendEventToKafka(key: NokkelInput, event: StatusoppdateringInput) {
        statusoppdateringKafkaProducer.sendEvent(key, event)
    }

    fun createStatusoppdateringForIdent(innloggetBruker: InnloggetBruker, dto: ProduceStatusoppdateringDto): Statusoppdatering {
        val now = LocalDateTime.now(ZoneOffset.UTC)
        val build = StatusoppdateringInputBuilder()
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
