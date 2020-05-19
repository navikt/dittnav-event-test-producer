package no.nav.personbruker.dittnav.eventtestproducer.innboks

import no.nav.brukernotifikasjon.schemas.Innboks
import no.nav.brukernotifikasjon.schemas.Nokkel
import no.nav.personbruker.dittnav.eventtestproducer.common.InnloggetBruker
import no.nav.personbruker.dittnav.eventtestproducer.common.createKeyForEvent
import no.nav.personbruker.dittnav.eventtestproducer.config.Environment
import no.nav.personbruker.dittnav.eventtestproducer.config.EventType
import no.nav.personbruker.dittnav.eventtestproducer.config.Kafka
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.LoggerFactory
import java.time.Instant

class InnboksProducer(private val env: Environment) {

    private val log = LoggerFactory.getLogger(InnboksProducer::class.java)
    private val kafkaProducer = KafkaProducer<Nokkel, Innboks>(Kafka.producerProps(env, EventType.INNBOKS))

    fun produceInnboksEventForIdent(innloggetBruker: InnloggetBruker, dto: ProduceInnboksDto) {
        val key = createKeyForEvent(env.systemUserName)
        val value = createInnboksForIdent(innloggetBruker, dto)

        produceEvent(innloggetBruker, key, value)
    }

    fun produceEvent(innloggetBruker: InnloggetBruker, key: Nokkel, value: Innboks) {
        try {
            kafkaProducer.send(ProducerRecord(Kafka.innboksTopicName, key, value))

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

    fun createInnboksForIdent(innloggetBruker: InnloggetBruker, dto: ProduceInnboksDto): Innboks {
        val nowInMs = Instant.now().toEpochMilli()

        return Innboks.newBuilder()
                .setFodselsnummer(innloggetBruker.ident)
                .setGrupperingsId("100$nowInMs")
                .setLink(dto.link)
                .setTekst(dto.tekst)
                .setTidspunkt(nowInMs)
                .setSikkerhetsnivaa(innloggetBruker.innloggingsnivaa)
                .build()
    }
}
