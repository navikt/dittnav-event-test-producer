package no.nav.personbruker.dittnav.eventtestproducer.innboks

import no.nav.personbruker.dittnav.eventtestproducer.config.Environment
import no.nav.personbruker.dittnav.eventtestproducer.config.Kafka
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.LoggerFactory
import no.nav.brukernotifikasjon.schemas.Innboks
import no.nav.brukernotifikasjon.schemas.Nokkel
import no.nav.personbruker.dittnav.eventtestproducer.common.InnloggetBruker
import no.nav.personbruker.dittnav.eventtestproducer.common.createKeyForEvent
import java.time.Instant

object InnboksProducer {
    private val log = LoggerFactory.getLogger(InnboksProducer::class.java)

    fun produceInnboksEventForIdent(innloggetBruker: InnloggetBruker, dto: ProduceInnboksDto) {
        KafkaProducer<Nokkel, Innboks>(Kafka.producerProps(Environment())).use { producer ->
            producer.send(ProducerRecord(Kafka.innboksTopicName, createKeyForEvent(), createInnboksForIdent(innloggetBruker, dto)))
        }
        log.info("Har produsert et innboks-event for identen: ${innloggetBruker.getIdent()}")
    }

    private fun createInnboksForIdent(innloggetBruker: InnloggetBruker, dto: ProduceInnboksDto): Innboks {
        val nowInMs = Instant.now().toEpochMilli()

        return Innboks.newBuilder()
                .setFodselsnummer(innloggetBruker.getIdent())
                .setGrupperingsId("100$nowInMs")
                .setLink(dto.link)
                .setTekst(dto.tekst)
                .setTidspunkt(nowInMs)
                .build()
    }
  }