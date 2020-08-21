package no.nav.personbruker.dittnav.eventtestproducer.statusoppdatering

import no.nav.brukernotifikasjon.schemas.Nokkel
import no.nav.brukernotifikasjon.schemas.Statusoppdatering
import no.nav.personbruker.dittnav.eventtestproducer.common.InnloggetBruker
import no.nav.personbruker.dittnav.eventtestproducer.common.createKeyForEvent
import no.nav.personbruker.dittnav.eventtestproducer.config.Environment
import no.nav.personbruker.dittnav.eventtestproducer.config.EventType
import no.nav.personbruker.dittnav.eventtestproducer.config.Kafka
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.LoggerFactory
import java.time.Instant

class StatusoppdateringProducer(private val env: Environment) {

    private val log = LoggerFactory.getLogger(StatusoppdateringProducer::class.java)

    private val kafkaProducer = KafkaProducer<Nokkel, Statusoppdatering>(Kafka.producerProps(env, EventType.STATUSOPPDATERING))

    fun produceStatusoppdateringEventForIdent(innloggetBruker: InnloggetBruker, dto: ProduceStatusoppdateringDto) {
        val key = createKeyForEvent(env.systemUserName)
        val value = createStatusoppdateringForIdent(innloggetBruker, dto)

        produceEvent(innloggetBruker, key, value)
    }

    fun produceEvent(innloggetBruker: InnloggetBruker, key: Nokkel, statusoppdatering: Statusoppdatering) {
        try {
            kafkaProducer.send(ProducerRecord(Kafka.statusoppdateringTopicName, key, statusoppdatering))

        } catch (e: Exception) {
            log.error("Det skjedde en feil ved produsering av et event for brukeren $innloggetBruker", e)
        }
    }

    fun close() {
        try {
            kafkaProducer.close()
            log.info("Produsenten er lukket.")

        } catch (e: Exception) {
            log.warn("Klarte ikke å lukke produsenten. Det kan være venter som ikke ble produsert.")
        }
    }

    fun createStatusoppdateringForIdent(innloggetBruker: InnloggetBruker, dto: ProduceStatusoppdateringDto): Statusoppdatering {
        val nowInMs = Instant.now().toEpochMilli()
        val build = Statusoppdatering.newBuilder()
                .setFodselsnummer(innloggetBruker.ident)
                .setGrupperingsId("100$nowInMs")
                .setLink(dto.link)
                .setTidspunkt(nowInMs)
                .setSikkerhetsnivaa(innloggetBruker.innloggingsnivaa)
                .setStatusGlobal(dto.statusGlobal)
                .setStatusIntern(dto.statusIntern)
                .setSakstema(dto.sakstema)
        return build.build()
    }

}
