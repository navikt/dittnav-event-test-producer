package no.nav.personbruker.dittnav.eventtestproducer.innboks

import java.sql.Connection
import java.sql.ResultSet
import java.time.ZoneId
import java.time.ZonedDateTime

fun Connection.getAllInnboksByFodselsnummer(fodselsnummer: String): List<Innboks> =
        prepareStatement("""SELECT * FROM INNBOKS WHERE fodselsnummer = ?""")
                .use {
                    it.setString(1, fodselsnummer)
                    it.executeQuery().list {
                        toInnboks()
                    }
                }

fun Connection.getInnboksByFodselsnummer(fodselsnummer: String): List<Innboks> =
        prepareStatement("""SELECT * FROM INNBOKS WHERE fodselsnummer = ? AND aktiv = true""")
                .use {
                    it.setString(1, fodselsnummer)
                    it.executeQuery().list {
                        toInnboks()
                    }
                }

private fun ResultSet.toInnboks(): Innboks {
    return Innboks(
            id = getInt("id"),
            produsent = getString("produsent"),
            eventTidspunkt = ZonedDateTime.ofInstant(getTimestamp("eventTidspunkt").toInstant(), ZoneId.of("Europe/Oslo")),
            fodselsnummer = getString("fodselsnummer"),
            eventId = getString("eventId"),
            grupperingsId = getString("grupperingsId"),
            tekst = getString("tekst"),
            link = getString("link"),
            sikkerhetsnivaa = getInt("sikkerhetsnivaa"),
            sistOppdatert = ZonedDateTime.ofInstant(getTimestamp("sistOppdatert").toInstant(), ZoneId.of("Europe/Oslo")),
            aktiv = getBoolean("aktiv")
    )
}

private fun <T> ResultSet.list(result: ResultSet.() -> T): List<T> =
        mutableListOf<T>().apply {
            while (next()) {
                add(result())
            }
        }
