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
    private val env = Environment()

    fun produceOppgaveEventForIdent(innloggetBruker: InnloggetBruker, dto: ProduceOppgaveDto) {
        val key = createKeyForEvent(env.systemUserName)
        val value = createOppgaveForIdent(innloggetBruker, dto)
        KafkaProducer<Nokkel, Oppgave>(Kafka.producerProps(Environment())).use { producer ->
            producer.send(ProducerRecord(oppgaveTopicName, key, value))
        }
        log.info("Har produsert et oppgace-event for identen: ${innloggetBruker.getIdent()}")
    }

    private fun createOppgaveForIdent(innloggetBruker: InnloggetBruker, dto: ProduceOppgaveDto): Oppgave {
        val nowInMs = Instant.now().toEpochMilli()
        val build = Oppgave.newBuilder()
                .setFodselsnummer(innloggetBruker.getIdent())
                .setGrupperingsId("100$nowInMs")
                .setLink(dto.link)
                .setTekst(dto.tekst)
                .setTidspunkt(nowInMs)
                .setSikkerhetsnivaa(innloggetBruker.getInnloggingsnivaa())
        return build.build()
    }


}
