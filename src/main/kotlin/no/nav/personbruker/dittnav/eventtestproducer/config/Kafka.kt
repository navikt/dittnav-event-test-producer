package no.nav.personbruker.dittnav.eventtestproducer.config

import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig
import io.confluent.kafka.serializers.KafkaAvroSerializer
import io.netty.util.NetUtil.getHostname
import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.config.SaslConfigs
import org.apache.kafka.common.config.SslConfigs
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.net.InetSocketAddress
import java.util.*

object Kafka {

    private val log: Logger = LoggerFactory.getLogger(Kafka::class.java)

    val doneTopicName = "aapen-brukernotifikasjon-done-v1"
    val beskjedTopicName = "aapen-brukernotifikasjon-nyBeskjed-v1"
    val innboksTopicName = "aapen-brukernotifikasjon-nyInnboks-v1"
    val oppgaveTopicName = "aapen-brukernotifikasjon-nyOppgave-v1"
    val statusoppdateringTopicName = "aapen-brukernotifikasjon-nyStatusoppdatering-v1"


    private fun credentialProps(env: Environment): Properties {
        return Properties().apply {
            put(SaslConfigs.SASL_MECHANISM, "PLAIN")
            put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT")
            put(SaslConfigs.SASL_JAAS_CONFIG,
                    """org.apache.kafka.common.security.plain.PlainLoginModule required username="${env.systemUserName}" password="${env.systemUserPassword}";""")
            System.getenv("NAV_TRUSTSTORE_PATH")?.let {
                put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_SSL")
                put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, File(it).absolutePath)
                put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, System.getenv("NAV_TRUSTSTORE_PASSWORD"))
                log.info("Configured ${SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG} location")
            }
        }
    }

    fun producerProps(env: Environment, eventTypeToProduce: EventType): Properties {
        val clientId = "${getHostname(InetSocketAddress(0))}-${eventTypeToProduce.eventType}"
        return Properties().apply {
            put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, env.bootstrapServers)
            put(KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, env.schemaRegistryUrl)
            put(ConsumerConfig.CLIENT_ID_CONFIG, clientId)
            put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer::class.java)
            put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer::class.java)
            if (ConfigUtil.isCurrentlyRunningOnNais()) {
                putAll(credentialProps(env))
            }
        }
    }

}
