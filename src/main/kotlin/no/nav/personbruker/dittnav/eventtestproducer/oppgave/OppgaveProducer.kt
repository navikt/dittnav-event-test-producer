package no.nav.personbruker.dittnav.eventtestproducer.oppgave

import no.nav.brukernotifikasjon.schemas.Nokkel
import no.nav.brukernotifikasjon.schemas.Oppgave
import no.nav.personbruker.dittnav.eventtestproducer.common.InnloggetBruker
import no.nav.personbruker.dittnav.eventtestproducer.common.createKeyForEvent
import no.nav.personbruker.dittnav.eventtestproducer.config.Environment
import no.nav.personbruker.dittnav.eventtestproducer.config.Kafka
import no.nav.personbruker.dittnav.eventtestproducer.config.Kafka.oppgaveTopicName
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.LoggerFactory
import java.time.Instant

object OppgaveProducer {

    private val log = LoggerFactory.getLogger(OppgaveProducer::class.java)

    fun produceOppgaveEventForIdent(innloggetBruker: InnloggetBruker, dto: ProduceOppgaveDto) {
        KafkaProducer<Nokkel, Oppgave>(Kafka.producerProps(Environment())).use { producer ->
            producer.send(ProducerRecord(oppgaveTopicName, createKeyForEvent(),createOppgaveForIdent(innloggetBruker, dto)))
        }
        log.info("Har produsert et oppgace-event for identen: ${innloggetBruker.getIdentFromToken()}")
    }

    private fun createOppgaveForIdent(innloggetBruker: InnloggetBruker, dto: ProduceOppgaveDto): Oppgave {
        val nowInMs = Instant.now().toEpochMilli()
        val build = Oppgave.newBuilder()
                .setFodselsnummer(innloggetBruker.getIdentFromToken())
                .setGrupperingsId("100$nowInMs")
                .setLink(dto.link)
                .setTekst(dto.tekst)
                .setTidspunkt(nowInMs)
        return build.build()
    }



}
