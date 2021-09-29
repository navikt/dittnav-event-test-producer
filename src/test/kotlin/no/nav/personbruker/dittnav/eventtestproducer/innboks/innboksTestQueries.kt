package no.nav.personbruker.dittnav.eventtestproducer.innboks

import java.sql.Connection
import java.sql.Types

fun Connection.createInnboks(innbokser: List<Innboks>) =
        prepareStatement("""INSERT INTO innboks(id, systembruker, eventTidspunkt, fodselsnummer, eventId, grupperingsId, tekst, link, sikkerhetsnivaa, sistOppdatert, aktiv, eksternVarsling, prefererteKanaler)
            VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)""")
                .use {
                    innbokser.forEach { innboks ->
                        run {
                            it.setInt(1, innboks.id)
                            it.setString(2, innboks.systembruker)
                            it.setObject(3, innboks.eventTidspunkt.toLocalDateTime(), Types.TIMESTAMP)
                            it.setString(4, innboks.fodselsnummer)
                            it.setString(5, innboks.eventId)
                            it.setString(6, innboks.grupperingsId)
                            it.setString(7, innboks.tekst)
                            it.setString(8, innboks.link)
                            it.setInt(9, innboks.sikkerhetsnivaa)
                            it.setObject(10, innboks.sistOppdatert.toLocalDateTime(), Types.TIMESTAMP)
                            it.setBoolean(11, innboks.aktiv)
                            it.setBoolean(12, innboks.eksternVarsling)
                            it.setObject(13, innboks.prefererteKanaler.joinToString(","))
                            it.addBatch()
                        }
                    }
                    it.executeBatch()
                }

fun Connection.deleteInnboks(innbokser: List<Innboks>) =
        prepareStatement("""DELETE FROM innboks WHERE eventId = ?""")
                .use {
                    innbokser.forEach { innboks ->
                        run {
                            it.setString(1, innboks.eventId)
                            it.addBatch()
                        }
                    }
                    it.executeBatch()
                }
