package no.nav.personbruker.dittnav.eventtestproducer.oppgave

import no.nav.personbruker.dittnav.eventtestproducer.common.InnloggetBruker
import no.nav.personbruker.dittnav.eventtestproducer.common.database.getUtcTimeStamp
import no.nav.personbruker.dittnav.eventtestproducer.common.database.map
import java.sql.Connection
import java.sql.ResultSet
import java.time.ZoneId
import java.time.ZonedDateTime

fun Connection.getAllOppgaveByFodselsnummer(innloggetBruker: InnloggetBruker): List<Oppgave> =
        prepareStatement("""SELECT * FROM OPPGAVE WHERE fodselsnummer = ?""")
                .use {
                    it.setString(1, innloggetBruker.ident)
                    it.executeQuery().map {
                        toOppgave()
                    }
                }

fun Connection.getAktivOppgaveByFodselsnummer(innloggetBruker: InnloggetBruker): List<Oppgave> =
        prepareStatement("""SELECT * FROM OPPGAVE WHERE fodselsnummer = ? AND aktiv = true""")
                .use {
                    it.setString(1, innloggetBruker.ident)
                    it.executeQuery().map {
                        toOppgave()
                    }
                }

private fun ResultSet.toOppgave(): Oppgave {
    return Oppgave(
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
