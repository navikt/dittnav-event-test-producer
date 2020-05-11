package no.nav.personbruker.dittnav.eventtestproducer.oppgave

import java.sql.Connection
import java.sql.Types

fun Connection.createOppgave(oppgaver: List<Oppgave>) =
        prepareStatement("""INSERT INTO oppgave(id, systembruker, eventTidspunkt, fodselsnummer, eventId, grupperingsId, tekst, link, sikkerhetsnivaa, sistOppdatert, aktiv)
            VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)""")
                .use {
                    oppgaver.forEach { oppgave ->
                        run {
                            it.setInt(1, oppgave.id)
                            it.setString(2, oppgave.systembruker)
                            it.setObject(3, oppgave.eventTidspunkt.toLocalDateTime(), Types.TIMESTAMP)
                            it.setString(4, oppgave.fodselsnummer)
                            it.setString(5, oppgave.eventId)
                            it.setString(6, oppgave.grupperingsId)
                            it.setString(7, oppgave.tekst)
                            it.setString(8, oppgave.link)
                            it.setInt(9, oppgave.sikkerhetsnivaa)
                            it.setObject(10, oppgave.sistOppdatert.toLocalDateTime(), Types.TIMESTAMP)
                            it.setBoolean(11, oppgave.aktiv)
                            it.addBatch()
                        }
                    }
                    it.executeBatch()
                }

fun Connection.deleteOppgave(oppgaver: List<Oppgave>) =
        prepareStatement("""DELETE FROM oppgave WHERE eventId = ?""")
                .use {
                    oppgaver.forEach { oppgave ->
                        run {
                            it.setString(1, oppgave.eventId)
                            it.addBatch()
                        }
                    }
                    it.executeBatch()
                }
