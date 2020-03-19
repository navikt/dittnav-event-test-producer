package no.nav.personbruker.dittnav.eventtestproducer.config

data class Environment(val bootstrapServers: String = getEnvVar("KAFKA_BOOTSTRAP_SERVERS"),
                       val schemaRegistryUrl: String = getEnvVar("KAFKA_SCHEMAREGISTRY_SERVERS"),
                       val systemUserName: String = getEnvVar("SERVICEUSER_USERNAME"),
                       val systemUserPassword: String = getEnvVar("SERVICEUSER_PASSWORD"),
                       val groupId: String = getEnvVar("GROUP_ID"),
                       val dbHost: String = getEnvVar("DB_HOST"),
                       val dbName: String = getEnvVar("DB_NAME"),
                       val dbUser: String = "$dbName-user",
                       val dbReadOnlyUser: String = "$dbName-readonly",
                       val dbUrl: String = "jdbc:postgresql://$dbHost/$dbName",
                       val dbMountPath: String = getEnvVar("DB_MOUNT_PATH"),
                       val corsAllowedOrigins: String = getEnvVar("CORS_ALLOWED_ORIGINS")
)

fun getEnvVar(varName: String): String {
    return System.getenv(varName)
            ?: throw IllegalArgumentException("Appen kan ikke starte uten av miljøvariabelen $varName er satt.")
}
