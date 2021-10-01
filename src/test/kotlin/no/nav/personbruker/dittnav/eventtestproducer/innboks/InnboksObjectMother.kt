package no.nav.personbruker.dittnav.eventtestproducer.innboks

import java.time.ZoneId
import java.time.ZonedDateTime

object InnboksObjectMother {
    fun createInnboks(id: Int, eventId: String, fodselsnummer: String, aktiv: Boolean, eksternVarsling: Boolean, prefererteKanaler: List<String>): Innboks {
        return Innboks(
                id = id,
                systembruker = "x-dittnav",
                eventTidspunkt = ZonedDateTime.now(ZoneId.of("Europe/Oslo")),
                fodselsnummer = fodselsnummer,
                eventId = eventId,
                grupperingsId = "100$fodselsnummer",
                tekst = "Dette er melding til brukeren",
                link = "https://nav.no/systemX/$fodselsnummer",
                sistOppdatert = ZonedDateTime.now(ZoneId.of("Europe/Oslo")),
                sikkerhetsnivaa = 4,
                aktiv = aktiv,
                eksternVarsling = eksternVarsling,
                prefererteKanaler = prefererteKanaler)
    }
}
