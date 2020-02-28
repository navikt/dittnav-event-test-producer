package no.nav.personbruker.dittnav.eventtestproducer.config

data class Environment(val bootstrapServers: String = getEnvVar("KAFKA_BOOTSTRAP_SERVERS", "localhost:29092"),
                       val schemaRegistryUrl: String = getEnvVar("KAFKA_SCHEMAREGISTRY_SERVERS", "http://localhost:8081"),
                       val systemUserName: String = getEnvVar("FSS_SYSTEMUSER_USERNAME", "username"),
                       val systemUserPassword: String = getEnvVar("FSS_SYSTEMUSER_PASSWORD", "password"),
                       val groupId: String = getEnvVar("GROUP_ID", "dittnav_events"),
                       val dbHost: String = getEnvVar("DB_HOST", "localhost:5432"),
                       val dbName: String = getEnvVar("DB_NAME", "dittnav-event-cache-preprod"),
                       val dbUser: String = getEnvVar("DB_NAME", "test") + "-user",
                       val dbReadOnlyUser: String = getEnvVar("DB_NAME", "test") + "-readonly",
                       val dbUrl: String = "jdbc:postgresql://$dbHost/$dbName",
                       val dbPassword: String = getEnvVar("DB_PASSWORD", "testpassword"),
                       val dbMountPath: String = getEnvVar("DB_MOUNT_PATH", "notUsedOnLocalhost"),
                       val corsAllowedOrigins: String = getEnvVar("CORS_ALLOWED_ORIGINS")
)

fun getEnvVar(varName: String, defaultValue: String? = null): String {
    val varValue = System.getenv(varName) ?: defaultValue
    return varValue ?: throw IllegalArgumentException("Variable $varName cannot be empty")
}
