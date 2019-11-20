package no.nav.personbruker.dittnav.eventtestproducer.informasjon

import no.nav.brukernotifikasjon.schemas.Informasjon
import no.nav.personbruker.dittnav.eventtestproducer.config.Environment
import no.nav.personbruker.dittnav.eventtestproducer.config.Kafka
import no.nav.personbruker.dittnav.eventtestproducer.config.Kafka.informasjonTopicName
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.LoggerFactory
import java.time.Instant

object InformasjonProducer {

    private val log = LoggerFactory.getLogger(InformasjonProducer::class.java)

    fun produceInformasjonEventForIdent(ident: String, dto: ProduceInformasjonDto) {
        KafkaProducer<String, Informasjon>(Kafka.producerProps(Environment())).use { producer ->
            producer.send(ProducerRecord(informasjonTopicName, createInformasjonForIdent(ident, dto)))
        }
        log.info("Har produsert et informasjons-event for identen: $ident")
    }

    private fun createInformasjonForIdent(ident: String, dto: ProduceInformasjonDto): Informasjon {
        val nowInMs = Instant.now().toEpochMilli()
        val build = Informasjon.newBuilder()
                .setAktorId(ident)
                .setDokumentId("100$nowInMs")
                .setEventId("$nowInMs")
                .setProdusent("DittNAV")
                .setLink(dto.link)
                .setTekst(dto.tekst)
                .setTidspunkt(nowInMs)
                .setSikkerhetsnivaa(4)
        return build.build()
    }

}
