plugins {
    // Apply the Kotlin JVM plugin to add support for Kotlin on the JVM.
    kotlin("jvm").version(Kotlin.version)
    kotlin("plugin.allopen").version(Kotlin.version)

    id(Shadow.pluginId) version (Shadow.version)

    // Apply the application plugin to add support for building a CLI application.
    application
}

repositories {
    // Use jcenter for resolving your dependencies.
    // You can declare any Maven/Ivy/file repository here.
    jcenter()
    maven("https://packages.confluent.io/maven")
    mavenLocal()
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.navikt:brukernotifikasjon-schemas:ekstern-varsling-SNAPSHOT")
    implementation(Hikari.cp)
    implementation(Jackson.dataTypeJsr310)
    implementation(Kafka.Apache.clients)
    implementation(Kafka.Confluent.avroSerializer)
    implementation(Ktor.auth)
    implementation(Ktor.authJwt)
    implementation(Ktor.jackson)
    implementation(Ktor.serverNetty)
    implementation(Logback.classic)
    implementation(Logstash.logbackEncoder)
    implementation(NAV.tokenValidatorKtor)
    implementation(NAV.vaultJdbc)
    implementation(Postgresql.postgresql)
    implementation(Prometheus.common)
    implementation(Prometheus.hotspot)
    implementation(Prometheus.logback)

    testImplementation(kotlin("test-junit5"))
    testImplementation(Bouncycastle.bcprovJdk15on)
    testImplementation(H2Database.h2)
    testImplementation(Jjwt.api)
    testImplementation(Jjwt.impl)
    testImplementation(Jjwt.jackson)
    testImplementation(Junit.api)
    testImplementation(Junit.engine)
    testImplementation(Kafka.Apache.kafka_2_12)
    testImplementation(Kafka.Apache.streams)
    testImplementation(Kafka.Confluent.schemaRegistry)
    testImplementation(Kluent.kluent)
    testImplementation(Ktor.clientMock)
    testImplementation(Mockk.mockk)
    testImplementation(NAV.kafkaEmbedded)
}

application {
    mainClassName = "io.ktor.server.netty.EngineMain"
}

tasks {
    withType<Test> {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }

    register("runServer", JavaExec::class) {
        environment("CORS_ALLOWED_ORIGINS", "localhost:9002")

        environment("OIDC_CLAIM_CONTAINING_THE_IDENTITY", "pid")
        environment("OIDC_ISSUER", "http://localhost:9000")
        environment("OIDC_DISCOVERY_URL", "http://localhost:9000/.well-known/openid-configuration")
        environment("OIDC_ACCEPTED_AUDIENCE", "stubOidcClient")

        environment("KAFKA_BOOTSTRAP_SERVERS", "localhost:29092")
        environment("KAFKA_SCHEMAREGISTRY_SERVERS", "http://localhost:8081")
        environment("SERVICEUSER_USERNAME", "username")
        environment("SERVICEUSER_PASSWORD", "password")

        environment("DB_HOST", "localhost:5432")
        environment("DB_NAME", "dittnav-event-cache-preprod")
        environment("DB_PASSWORD", "testpassword")
        environment("DB_MOUNT_PATH", "notUsedOnLocalhost")

        main = application.mainClassName
        classpath = sourceSets["main"].runtimeClasspath
    }
}

apply(plugin = Shadow.pluginId)
