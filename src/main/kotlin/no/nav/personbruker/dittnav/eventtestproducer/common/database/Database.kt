package no.nav.personbruker.dittnav.eventtestproducer.common.database

import java.sql.Connection
import javax.sql.DataSource

interface Database {

    val dataSource: DataSource

    suspend fun <T> dbQuery(operationToExecute: Connection.() -> T): T =
            dataSource.connection.use { openConnection ->
                try {
                    openConnection.operationToExecute().apply {
                        openConnection.commit()
                    }

                } catch (e: Exception) {
                    try {
                        openConnection.rollback()
                    } catch (rollbackException: Exception) {
                        e.addSuppressed(rollbackException)
                    }
                    throw e
                }
            }
}
