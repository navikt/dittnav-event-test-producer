package no.nav.personbruker.dittnav.eventtestproducer.beskjed

import no.nav.brukernotifikasjon.schemas.Beskjed
import no.nav.personbruker.dittnav.eventtestproducer.config.Environment
import no.nav.personbruker.dittnav.eventtestproducer.config.Kafka
import no.nav.personbruker.dittnav.eventtestproducer.config.Kafka.beskjedTopicName
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.LoggerFactory
import java.time.Instant

object BeskjedProducer {

    private val log = LoggerFactory.getLogger(BeskjedProducer::class.java)

    fun produceBeskjedEventForIdent(ident: String, dto: ProduceBeskjedDto) {
        KafkaProducer<String, Beskjed>(Kafka.producerProps(Environment())).use { producer ->
            producer.send(ProducerRecord(beskjedTopicName, createBeskjedForIdent(ident, dto)))
        }
        log.info("Har produsert et beskjeds-event for identen: $ident")
    }

    private fun createBeskjedForIdent(ident: String, dto: ProduceBeskjedDto): Beskjed {
        val nowInMs = Instant.now().toEpochMilli()
        val build = Beskjed.newBuilder()
                .setFodselsnummer(ident)
                .setGrupperingsId("100$nowInMs")
                .setEventId("$nowInMs")
                .setProdusent("DittNAV")
                .setLink(dto.link)
                .setTekst(dto.tekst)
                .setTidspunkt(nowInMs)
                .setSikkerhetsnivaa(4)
        return build.build()
    }

}
