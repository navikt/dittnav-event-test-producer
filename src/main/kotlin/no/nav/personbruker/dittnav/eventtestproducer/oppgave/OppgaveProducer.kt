package no.nav.personbruker.dittnav.eventtestproducer.oppgave

import no.nav.brukernotifikasjon.schemas.Oppgave
import no.nav.personbruker.dittnav.eventtestproducer.config.Environment
import no.nav.personbruker.dittnav.eventtestproducer.config.Kafka
import no.nav.personbruker.dittnav.eventtestproducer.config.Kafka.oppgaveTopicName
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.LoggerFactory
import java.time.Instant

object OppgaveProducer {

    private val log = LoggerFactory.getLogger(OppgaveProducer::class.java)

    fun produceOppgaveEventForIdent(ident: String, dto: ProduceOppgaveDto) {
        KafkaProducer<String, Oppgave>(Kafka.producerProps(Environment())).use { producer ->
            producer.send(ProducerRecord(oppgaveTopicName, createOppgaveForIdent(ident, dto)))
        }
        log.info("Har produsert et oppgace-event for identen: $ident")
    }

    private fun createOppgaveForIdent(ident: String, dto: ProduceOppgaveDto): Oppgave {
        val nowInMs = Instant.now().toEpochMilli()
        val build = Oppgave.newBuilder()
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
