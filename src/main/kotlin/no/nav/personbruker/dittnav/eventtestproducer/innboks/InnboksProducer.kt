package no.nav.personbruker.dittnav.eventtestproducer.innboks

import no.nav.personbruker.dittnav.eventtestproducer.config.Environment
import no.nav.personbruker.dittnav.eventtestproducer.config.Kafka
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.LoggerFactory
import no.nav.brukernotifikasjon.schemas.Innboks
import java.time.Instant

object InnboksProducer {
    private val log = LoggerFactory.getLogger(InnboksProducer::class.java)

    fun produceInnboksEventForIdent(ident: String, dto: ProduceInnboksDto) {
        KafkaProducer<String, Innboks>(Kafka.producerProps(Environment())).use { producer ->
            producer.send(ProducerRecord(Kafka.innboksTopicName, createInnboksForIdent(ident, dto)))
        }
        log.info("Har produsert et innboks-event for identen: $ident")
    }

    private fun createInnboksForIdent(ident: String, dto: ProduceInnboksDto): Innboks {
        val nowInMs = Instant.now().toEpochMilli()

        return Innboks.newBuilder()
                .setAktorId(ident)
                .setDokumentId("100$nowInMs")
                .setEventId("$nowInMs")
                .setProdusent("DittNAV")
                .setLink(dto.link)
                .setTekst(dto.tekst)
                .setTidspunkt(nowInMs)
                .setSikkerhetsnivaa(4)
                .build()
    }
}