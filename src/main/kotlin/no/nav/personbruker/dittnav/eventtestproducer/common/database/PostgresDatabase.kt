package no.nav.personbruker.dittnav.eventtestproducer.common.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import no.nav.personbruker.dittnav.eventtestproducer.config.ConfigUtil
import no.nav.personbruker.dittnav.eventtestproducer.config.Environment
import no.nav.personbruker.dittnav.eventtestproducer.config.getEnvVar
import no.nav.vault.jdbc.hikaricp.HikariCPVaultUtil

class PostgresDatabase(env: Environment) : Database {

    private val envDataSource: HikariDataSource

    init {
        envDataSource = createCorrectConnectionForEnvironment(env)
    }

    override val dataSource: HikariDataSource
        get() = envDataSource


    private fun createCorrectConnectionForEnvironment(env: Environment): HikariDataSource {
        return when (ConfigUtil.isCurrentlyRunningOnNais()) {
            true -> createConnectionViaVaultWithDbUser(env)
            false -> createConnectionForLocalDbWithDbUser(env)
        }
    }

    private fun createConnectionForLocalDbWithDbUser(env: Environment): HikariDataSource {
        return hikariFromLocalDb(env, env.dbUser)
    }

    private fun createConnectionViaVaultWithDbUser(env: Environment): HikariDataSource {
        return hikariDatasourceViaVault(env, env.dbReadOnlyUser)
    }

    companion object {

        fun hikariFromLocalDb(env: Environment, dbUser: String): HikariDataSource {
            val dbPassword: String = getEnvVar("DB_PASSWORD")
            val config = hikariCommonConfig(env).apply {
                username = dbUser
                password = dbPassword
                validate()
            }
            return HikariDataSource(config)
        }

        fun hikariDatasourceViaVault(env: Environment, dbUser: String): HikariDataSource {
            val config = hikariCommonConfig(env)
            config.validate()
            return HikariCPVaultUtil.createHikariDataSourceWithVaultIntegration(config, env.dbMountPath, dbUser)
        }

        private fun hikariCommonConfig(env: Environment): HikariConfig {
            val config = HikariConfig().apply {
                driverClassName = "org.postgresql.Driver"
                jdbcUrl = env.dbUrl
                minimumIdle = 0
                maxLifetime = 30001
                maximumPoolSize = 2
                connectionTimeout = 500
                validationTimeout = 250
                idleTimeout = 10001
                isAutoCommit = false
                transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            }
            return config
        }
    }

}
