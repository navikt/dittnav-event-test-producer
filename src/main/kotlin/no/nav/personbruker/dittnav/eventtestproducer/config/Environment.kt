package no.nav.personbruker.dittnav.eventtestproducer.config

data class Environment(val bootstrapServers: String = getEnvVar("KAFKA_BOOTSTRAP_SERVERS"),
                       val schemaRegistryUrl: String = getEnvVar("KAFKA_SCHEMAREGISTRY_SERVERS"),
                       val systemUserName: String = getEnvVar("FSS_SYSTEMUSER_USERNAME"),
                       val systemUserPassword: String = getEnvVar("FSS_SYSTEMUSER_PASSWORD"),
                       val groupId: String = getEnvVar("GROUP_ID"),
                       val dbHost: String = getEnvVar("DB_HOST"),
                       val dbName: String = getEnvVar("DB_NAME"),
                       val dbUser: String = getEnvVar("DB_NAME") + "-user",
                       val dbReadOnlyUser: String = getEnvVar("DB_NAME") + "-readonly",
                       val dbUrl: String = "jdbc:postgresql://$dbHost/$dbName",
                       val dbPassword: String = getEnvVar("DB_PASSWORD"),
                       val dbMountPath: String = getEnvVar("DB_MOUNT_PATH"),
                       val corsAllowedOrigins: String = getEnvVar("CORS_ALLOWED_ORIGINS")
)

fun getEnvVar(varName: String): String {
    return System.getenv(varName)
            ?: throw IllegalArgumentException("Appen kan ikke starte uten av miljøvariabelen $varName er satt.")
}
