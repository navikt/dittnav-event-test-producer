package no.nav.personbruker.dittnav.eventtestproducer.oppgave

import java.sql.Connection
import java.sql.ResultSet
import java.time.ZoneId
import java.time.ZonedDateTime

fun Connection.getAllOppgaveByFodselsnummer(fodselsnummer: String): List<Oppgave> =
        prepareStatement("""SELECT * FROM OPPGAVE WHERE fodselsnummer = ?""")
                .use {
                    it.setString(1, fodselsnummer)
                    it.executeQuery().list {
                        toOppgave()
                    }
                }

fun Connection.getOppgaveByFodselsnummer(fodselsnummer: String): List<Oppgave> =
        prepareStatement("""SELECT * FROM OPPGAVE WHERE fodselsnummer = ? AND aktiv = true""")
                .use {
                    it.setString(1, fodselsnummer)
                    it.executeQuery().list {
                        toOppgave()
                    }
                }

private fun ResultSet.toOppgave(): Oppgave {
    return Oppgave(
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
