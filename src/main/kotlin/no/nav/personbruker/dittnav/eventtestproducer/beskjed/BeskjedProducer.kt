package no.nav.personbruker.dittnav.eventtestproducer.beskjed

import no.nav.brukernotifikasjon.schemas.Beskjed
import no.nav.brukernotifikasjon.schemas.Nokkel
import no.nav.personbruker.dittnav.eventtestproducer.common.createKeyForEvent
import no.nav.personbruker.dittnav.eventtestproducer.config.Environment
import no.nav.personbruker.dittnav.eventtestproducer.config.Kafka
import no.nav.personbruker.dittnav.eventtestproducer.config.Kafka.beskjedTopicName
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.LoggerFactory
import java.time.Instant
import java.time.temporal.ChronoUnit

object BeskjedProducer {

    private val log = LoggerFactory.getLogger(BeskjedProducer::class.java)

    fun produceBeskjedEventForIdent(ident: String, dto: ProduceBeskjedDto) {
        KafkaProducer<Nokkel, Beskjed>(Kafka.producerProps(Environment())).use { producer ->
            producer.send(ProducerRecord(beskjedTopicName, createKeyForEvent(), createBeskjedForIdent(ident, dto)))
        }
        log.info("Har produsert et beskjeds-event for identen: $ident")
    }

    private fun createBeskjedForIdent(ident: String, dto: ProduceBeskjedDto): Beskjed {
        val nowInMs = Instant.now().toEpochMilli()
        val weekFromNowInMs = Instant.now().plus(7, ChronoUnit.DAYS).toEpochMilli()
        val build = Beskjed.newBuilder()
                .setFodselsnummer(ident)
                .setGrupperingsId("100$nowInMs")
                .setLink(dto.link)
                .setTekst(dto.tekst)
                .setTidspunkt(nowInMs)
                .setSynligFremTil(weekFromNowInMs)
        return build.build()
    }

}
