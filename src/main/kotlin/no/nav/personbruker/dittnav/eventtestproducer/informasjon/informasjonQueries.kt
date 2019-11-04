package no.nav.personbruker.dittnav.eventtestproducer.informasjon

import Informasjon
import java.sql.Connection
import java.sql.ResultSet
import java.time.ZoneId
import java.time.ZonedDateTime

fun Connection.getAllInformasjonByAktorId(aktorId: String): List<Informasjon> =
        prepareStatement("""SELECT * FROM INFORMASJON WHERE aktorId = ?""")
                .use {
                    it.setString(1, aktorId)
                    it.executeQuery().list {
                        toInformasjon()
                    }
                }

fun Connection.getInformasjonByAktorId(aktorId: String): List<Informasjon> =
        prepareStatement("""SELECT * FROM INFORMASJON WHERE aktorId = ? AND aktiv = true""")
                .use {
                    it.setString(1, aktorId)
                    it.executeQuery().list {
                        toInformasjon()
                    }
                }

private fun ResultSet.toInformasjon(): Informasjon {
    return Informasjon(
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
