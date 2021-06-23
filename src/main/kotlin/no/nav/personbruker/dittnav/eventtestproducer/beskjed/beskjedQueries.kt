package no.nav.personbruker.dittnav.eventtestproducer.beskjed

import Beskjed
import no.nav.personbruker.dittnav.eventtestproducer.common.InnloggetBruker
import no.nav.personbruker.dittnav.eventtestproducer.common.database.getListFromSeparatedString
import no.nav.personbruker.dittnav.eventtestproducer.common.database.getNullableZonedDateTime
import no.nav.personbruker.dittnav.eventtestproducer.common.database.getUtcTimeStamp
import no.nav.personbruker.dittnav.eventtestproducer.common.database.map
import java.sql.Connection
import java.sql.ResultSet
import java.time.ZoneId
import java.time.ZonedDateTime

fun Connection.getAllBeskjedByFodselsnummer(innloggetBruker: InnloggetBruker): List<Beskjed> =
        prepareStatement("""SELECT * FROM BESKJED WHERE fodselsnummer = ?""")
                .use {
                    it.setString(1, innloggetBruker.ident)
                    it.executeQuery().map {
                        toBeskjed()
                    }
                }

fun Connection.getAktivBeskjedByFodselsnummer(innloggetBruker: InnloggetBruker): List<Beskjed> =
        prepareStatement("""SELECT * FROM BESKJED WHERE fodselsnummer = ? AND aktiv = true""")
                .use {
                    it.setString(1, innloggetBruker.ident)
                    it.executeQuery().map {
                        toBeskjed()
                    }
                }

fun ResultSet.toBeskjed(): Beskjed {
    return Beskjed(
            id = getInt("id"),
            uid = getString("uid"),
            fodselsnummer = getString("fodselsnummer"),
            grupperingsId = getString("grupperingsId"),
            eventId = getString("eventId"),
            eventTidspunkt = ZonedDateTime.ofInstant(getUtcTimeStamp("eventTidspunkt").toInstant(), ZoneId.of("Europe/Oslo")),
            systembruker = getString("systembruker"),
            sikkerhetsnivaa = getInt("sikkerhetsnivaa"),
            sistOppdatert = ZonedDateTime.ofInstant(getUtcTimeStamp("sistOppdatert").toInstant(), ZoneId.of("Europe/Oslo")),
            synligFremTil = getNullableZonedDateTime("synligFremTil"),
            tekst = getString("tekst"),
            link = getString("link"),
            aktiv = getBoolean("aktiv"),
            eksternVarsling = getBoolean("eksternVarsling"),
            prefererteKanaler = getListFromSeparatedString("prefererteKanaler", ",")
    )
}
