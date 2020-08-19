package no.nav.personbruker.dittnav.eventtestproducer.statusOppdatering

import no.nav.brukernotifikasjon.schemas.Nokkel
import no.nav.brukernotifikasjon.schemas.StatusOppdatering
import no.nav.personbruker.dittnav.eventtestproducer.common.InnloggetBruker
import no.nav.personbruker.dittnav.eventtestproducer.common.createKeyForEvent
import no.nav.personbruker.dittnav.eventtestproducer.config.Environment
import no.nav.personbruker.dittnav.eventtestproducer.config.EventType
import no.nav.personbruker.dittnav.eventtestproducer.config.Kafka
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.LoggerFactory
import java.time.Instant

class StatusOppdateringProducer(private val env: Environment) {

    private val log = LoggerFactory.getLogger(StatusOppdateringProducer::class.java)

    private val kafkaProducer = KafkaProducer<Nokkel, StatusOppdatering>(Kafka.producerProps(env, EventType.STATUSOPPDATERING))

    fun produceStatusOppdateringEventForIdent(innloggetBruker: InnloggetBruker, dto: ProduceStatusOppdateringDto) {
        val key = createKeyForEvent(env.systemUserName)
        val value = createStatusOppdateringForIdent(innloggetBruker, dto)

        produceEvent(innloggetBruker, key, value)
    }

    fun produceEvent(innloggetBruker: InnloggetBruker, key: Nokkel, statusOppdatering: StatusOppdatering) {
        try {
            kafkaProducer.send(ProducerRecord(Kafka.statusOppdateringTopicName, key, statusOppdatering))

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

    fun createStatusOppdateringForIdent(innloggetBruker: InnloggetBruker, dto: ProduceStatusOppdateringDto): StatusOppdatering {
        val nowInMs = Instant.now().toEpochMilli()
        val build = StatusOppdatering.newBuilder()
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
