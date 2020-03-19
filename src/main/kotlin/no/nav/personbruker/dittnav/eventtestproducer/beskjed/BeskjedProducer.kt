package no.nav.personbruker.dittnav.eventtestproducer.beskjed

import no.nav.brukernotifikasjon.schemas.Beskjed
import no.nav.brukernotifikasjon.schemas.Nokkel
import no.nav.personbruker.dittnav.eventtestproducer.common.InnloggetBruker
import no.nav.personbruker.dittnav.eventtestproducer.common.createKeyForEvent
import no.nav.personbruker.dittnav.eventtestproducer.config.Environment
import no.nav.personbruker.dittnav.eventtestproducer.config.EventType
import no.nav.personbruker.dittnav.eventtestproducer.config.Kafka
import no.nav.personbruker.dittnav.eventtestproducer.config.Kafka.beskjedTopicName
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.LoggerFactory
import java.time.Instant
import java.time.temporal.ChronoUnit

class BeskjedProducer(private val env: Environment) {

    private val log = LoggerFactory.getLogger(BeskjedProducer::class.java)

    private val kafkaProducer = KafkaProducer<Nokkel, Beskjed>(Kafka.producerProps(env, EventType.BESKJED))

    fun produceBeskjedEventForIdent(innloggetBruker: InnloggetBruker, dto: ProduceBeskjedDto) {
        val key = createKeyForEvent(env.systemUserName)
        val value = createBeskjedForIdent(innloggetBruker, dto)

        try {
            kafkaProducer.send(ProducerRecord(beskjedTopicName, key, value))
            log.info("Har produsert et beskjed-event for for brukeren: $innloggetBruker")

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

    private fun createBeskjedForIdent(innloggetBruker: InnloggetBruker, dto: ProduceBeskjedDto): Beskjed {
        val nowInMs = Instant.now().toEpochMilli()
        val weekFromNowInMs = Instant.now().plus(7, ChronoUnit.DAYS).toEpochMilli()
        val build = Beskjed.newBuilder()
                .setFodselsnummer(innloggetBruker.ident)
                .setGrupperingsId("100$nowInMs")
                .setLink(dto.link)
                .setTekst(dto.tekst)
                .setTidspunkt(nowInMs)
                .setSynligFremTil(weekFromNowInMs)
                .setSikkerhetsnivaa(innloggetBruker.innloggingsnivaa)
        return build.build()
    }

}
