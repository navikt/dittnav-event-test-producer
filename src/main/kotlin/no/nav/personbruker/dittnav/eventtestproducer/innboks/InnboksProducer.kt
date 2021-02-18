package no.nav.personbruker.dittnav.eventtestproducer.innboks

import no.nav.brukernotifikasjon.schemas.Innboks
import no.nav.brukernotifikasjon.schemas.Nokkel
import no.nav.personbruker.dittnav.eventtestproducer.common.kafka.KafkaProducerWrapper
import no.nav.personbruker.dittnav.eventtestproducer.common.InnloggetBruker
import no.nav.personbruker.dittnav.eventtestproducer.common.createKeyForEvent
import org.slf4j.LoggerFactory
import java.time.LocalDateTime
import java.time.ZoneOffset

class InnboksProducer(private val innboksKafkaProducer: KafkaProducerWrapper<Nokkel, Innboks>, private val systembruker: String) {

    private val log = LoggerFactory.getLogger(InnboksProducer::class.java)

    fun produceInnboksEventForIdent(innloggetBruker: InnloggetBruker, dto: ProduceInnboksDto) {
        try {
            val key = createKeyForEvent(systembruker)
            val event = createInnboksForIdent(innloggetBruker, dto)
            sendEventToKafka(key, event)
        } catch (e: Exception) {
            log.error("Det skjedde en feil ved produsering av et event for brukeren $innloggetBruker", e)
        }
    }

    fun sendEventToKafka(key: Nokkel, event: Innboks) {
        innboksKafkaProducer.sendEvent(key, event)
    }

    fun createInnboksForIdent(innloggetBruker: InnloggetBruker, dto: ProduceInnboksDto): Innboks {
        val nowInMs = LocalDateTime.now(ZoneOffset.UTC).toInstant(ZoneOffset.UTC).toEpochMilli()

        return Innboks.newBuilder()
                .setFodselsnummer(innloggetBruker.ident)
                .setGrupperingsId(dto.grupperingsid)
                .setLink(dto.link)
                .setTekst(dto.tekst)
                .setTidspunkt(nowInMs)
                .setSikkerhetsnivaa(innloggetBruker.innloggingsnivaa)
                .build()
    }
}
