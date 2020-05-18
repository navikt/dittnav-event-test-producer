package no.nav.personbruker.dittnav.eventtestproducer.innboks

import no.nav.personbruker.dittnav.eventtestproducer.common.InnloggetBruker
import no.nav.personbruker.dittnav.eventtestproducer.common.database.getUtcTimeStamp
import no.nav.personbruker.dittnav.eventtestproducer.common.database.map
import java.sql.Connection
import java.sql.ResultSet
import java.time.ZoneId
import java.time.ZonedDateTime

fun Connection.getAllInnboksByFodselsnummer(innloggetBruker: InnloggetBruker): List<Innboks> =
        prepareStatement("""SELECT * FROM INNBOKS WHERE fodselsnummer = ?""")
                .use {
                    it.setString(1, innloggetBruker.ident)
                    it.executeQuery().map {
                        toInnboks()
                    }
                }

fun Connection.getAktivInnboksByFodselsnummer(innloggetBruker: InnloggetBruker): List<Innboks> =
        prepareStatement("""SELECT * FROM INNBOKS WHERE fodselsnummer = ? AND aktiv = true""")
                .use {
                    it.setString(1, innloggetBruker.ident)
                    it.executeQuery().map {
                        toInnboks()
                    }
                }

private fun ResultSet.toInnboks(): Innboks {
    return Innboks(
            id = getInt("id"),
            systembruker = getString("systembruker"),
            eventTidspunkt = ZonedDateTime.ofInstant(getUtcTimeStamp("eventTidspunkt").toInstant(), ZoneId.of("Europe/Oslo")),
            fodselsnummer = getString("fodselsnummer"),
            eventId = getString("eventId"),
            grupperingsId = getString("grupperingsId"),
            tekst = getString("tekst"),
            link = getString("link"),
            sikkerhetsnivaa = getInt("sikkerhetsnivaa"),
            sistOppdatert = ZonedDateTime.ofInstant(getUtcTimeStamp("sistOppdatert").toInstant(), ZoneId.of("Europe/Oslo")),
            aktiv = getBoolean("aktiv")
    )
}
