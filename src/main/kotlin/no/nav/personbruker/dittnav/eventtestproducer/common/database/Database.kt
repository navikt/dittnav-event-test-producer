package no.nav.personbruker.dittnav.eventtestproducer.common.database

import com.zaxxer.hikari.HikariDataSource
import java.sql.Connection

interface Database {

    val dataSource: HikariDataSource

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
