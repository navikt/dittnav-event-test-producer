package no.nav.personbruker.dittnav.eventtestproducer.oppgave

import java.sql.Connection
import java.sql.ResultSet
import java.time.ZoneId
import java.time.ZonedDateTime

fun Connection.getAllOppgaveByAktorId(aktorId: String): List<Oppgave> =
        prepareStatement("""SELECT * FROM OPPGAVE WHERE aktorId = ?""")
                .use {
                    it.setString(1, aktorId)
                    it.executeQuery().list {
                        toOppgave()
                    }
                }

fun Connection.getOppgaveByAktorId(aktorId: String): List<Oppgave> =
        prepareStatement("""SELECT * FROM OPPGAVE WHERE aktorId = ? AND aktiv = true""")
                .use {
                    it.setString(1, aktorId)
                    it.executeQuery().list {
                        toOppgave()
                    }
                }

private fun ResultSet.toOppgave(): Oppgave {
    return Oppgave(
            id = getInt("id"),
            produsent = getString("produsent"),
            eventTidspunkt = ZonedDateTime.ofInstant(getTimestamp("eventTidspunkt").toInstant(), ZoneId.of("Europe/Oslo")),
            aktorId = getString("aktorId"),
            eventId = getString("eventId"),
            dokumentId = getString("dokumentId"),
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
