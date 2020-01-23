import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val prometheusVersion = "0.6.0"
val ktorVersion = "1.3.0"
val junitVersion = "5.4.1"
val kafkaVersion = "2.2.0"
val confluentVersion = "5.2.0"
val brukernotifikasjonSchemaVersion = "1.2019.11.28-08.11-a783ab64f2d6"
val logstashVersion = 5.2
val logbackVersion = "1.2.3"
val vaultJdbcVersion = "1.3.1"
val flywayVersion = "5.2.4"
val hikariCPVersion = "3.2.0"
val postgresVersion = "42.2.5"
val h2Version = "1.4.199"
val jacksonVersion = "2.9.9"
val kluentVersion = "1.52"

plugins {
    // Apply the Kotlin JVM plugin to add support for Kotlin on the JVM.
    val kotlinVersion = "1.3.50"
    kotlin("jvm").version(kotlinVersion)
    kotlin("plugin.allopen").version(kotlinVersion)

    id("org.flywaydb.flyway") version("5.2.4")

    // Apply the application plugin to add support for building a CLI application.
    application
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

repositories {
    // Use jcenter for resolving your dependencies.
    // You can declare any Maven/Ivy/file repository here.
    jcenter()
    maven("http://packages.confluent.io/maven")
    mavenLocal()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    compile("no.nav.security:token-validation-ktor:1.1.0")
    compile("no.nav:vault-jdbc:$vaultJdbcVersion")
    compile("com.zaxxer:HikariCP:$hikariCPVersion")
    compile("org.postgresql:postgresql:$postgresVersion")
    compile("org.flywaydb:flyway-core:$flywayVersion")
    compile("ch.qos.logback:logback-classic:$logbackVersion")
    compile("net.logstash.logback:logstash-logback-encoder:$logstashVersion")
    compile("io.prometheus:simpleclient_common:$prometheusVersion")
    compile("io.prometheus:simpleclient_hotspot:$prometheusVersion")
    compile("io.ktor:ktor-server-netty:$ktorVersion")
    compile("io.ktor:ktor-auth:$ktorVersion")
    compile("io.ktor:ktor-auth-jwt:$ktorVersion")
    compile("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")
    compile("io.ktor:ktor-jackson:$ktorVersion")
    compile("org.apache.kafka:kafka-clients:$kafkaVersion")
    compile("io.confluent:kafka-avro-serializer:$confluentVersion")
    compile("no.nav:brukernotifikasjon-schemas:$brukernotifikasjonSchemaVersion")
    testCompile("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testCompile(kotlin("test-junit5"))
    testImplementation("no.nav:kafka-embedded-env:2.1.1")
    testImplementation("org.apache.kafka:kafka_2.12:$kafkaVersion")
    testImplementation("org.apache.kafka:kafka-streams:$kafkaVersion")
    testImplementation("io.confluent:kafka-schema-registry:$confluentVersion")
    testImplementation("com.h2database:h2:$h2Version")
    testImplementation("org.amshove.kluent:kluent:$kluentVersion")
    testRuntime("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}

application {
    mainClassName = "io.ktor.server.netty.EngineMain"
}

tasks {
    withType<Jar> {
        manifest {
            attributes["Main-Class"] = application.mainClassName
        }
        from(configurations.runtime.get().map { if (it.isDirectory) it else zipTree(it) })
    }

    withType<Test> {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }

    register("runServer", JavaExec::class) {
        environment("CORS_ALLOWED_ORIGINS", "localhost:9002")

        main = application.mainClassName
        classpath = sourceSets["main"].runtimeClasspath
    }
}
