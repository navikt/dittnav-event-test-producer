package no.nav.personbruker.dittnav.eventtestproducer.ytelsestesting

import kotlinx.coroutines.runBlocking
import no.nav.brukernotifikasjon.schemas.*
import no.nav.personbruker.dittnav.eventtestproducer.common.kafka.KafkaProducerWrapper
import no.nav.personbruker.dittnav.eventtestproducer.beskjed.BeskjedProducer
import no.nav.personbruker.dittnav.eventtestproducer.config.Environment
import no.nav.personbruker.dittnav.eventtestproducer.config.EventType
import no.nav.personbruker.dittnav.eventtestproducer.config.Kafka
import no.nav.personbruker.dittnav.eventtestproducer.done.DoneProducer
import no.nav.personbruker.dittnav.eventtestproducer.innboks.InnboksProducer
import no.nav.personbruker.dittnav.eventtestproducer.oppgave.OppgaveProducer
import no.nav.personbruker.dittnav.eventtestproducer.statusoppdatering.StatusoppdateringProducer
import org.apache.kafka.clients.producer.KafkaProducer
import org.junit.jupiter.api.Test

internal class TestDataServiceTest {

    private val env = createPropertiesForTestEnvironment()

    private val kafkaProducerDone = KafkaProducerWrapper(Kafka.doneTopicName, KafkaProducer<Nokkel, Done>(Kafka.producerProps(env, EventType.DONE)))
    private val doneProducer = DoneProducer(kafkaProducerDone, env.systemUserName)

    private val kafkaProducerBeskjed = KafkaProducerWrapper(Kafka.beskjedTopicName, KafkaProducer<Nokkel, Beskjed>(Kafka.producerProps(env, EventType.BESKJED)))
    private val beskjedProducer = BeskjedProducer(kafkaProducerBeskjed, env.systemUserName)

    private val kafkaProducerInnboks = KafkaProducerWrapper(Kafka.innboksTopicName, KafkaProducer<Nokkel, Innboks>(Kafka.producerProps(env, EventType.INNBOKS)))
    private val innboksProducer = InnboksProducer(kafkaProducerInnboks, env.systemUserName)

    private val kafkaProducerOppgave = KafkaProducerWrapper(Kafka.oppgaveTopicName, KafkaProducer<Nokkel, Oppgave>(Kafka.producerProps(env, EventType.OPPGAVE)))
    private val oppgaveProducer = OppgaveProducer(kafkaProducerOppgave, env.systemUserName)

    private val kafkaProducerStatusoppdatering = KafkaProducerWrapper(Kafka.statusoppdateringTopicName, KafkaProducer<Nokkel, Statusoppdatering>(Kafka.producerProps(env, EventType.STATUSOPPDATERING)))
    private val statusoppdateringProducer = StatusoppdateringProducer(kafkaProducerStatusoppdatering, env.systemUserName)

    //@Test
    fun produserTestcaseUtenEksternVarsling() {
        val testDataService = TestDataService(doneProducer, beskjedProducer, oppgaveProducer, innboksProducer, statusoppdateringProducer)
        runBlocking {
            testDataService.produserBeskjeder(produceDone = true, YTestDto())
            testDataService.produserOppgaver(produceDone = true, YTestDto())
            testDataService.produserInnboks(produceDone = true, YTestDto())
            testDataService.produserStatusoppdateringer(YTestDto())
        }
    }

    //@Test
    fun produserTestcaseMedEksternVarsling() {
        val testDataService = TestDataService(doneProducer, beskjedProducer, oppgaveProducer, innboksProducer, statusoppdateringProducer)
        runBlocking {
            testDataService.produserBeskjeder(produceDone = false, YTestDto(eksternVarsling = true))
            testDataService.produserOppgaver(produceDone = false, YTestDto(eksternVarsling = true))
        }
    }


    private fun createPropertiesForTestEnvironment(): Environment {
        return Environment(
                bootstrapServers = "localhost:29092",
                schemaRegistryUrl = "http://localhost:8081",
                systemUserName = "username",
                systemUserPassword = "password",
                dbHost = "localhost:5432",
                dbName = "dittnav-event-cache-preprod",
                dbMountPath = "notUsedOnLocalhost",
                corsAllowedOrigins = "localhost:9002"
        )
    }

}
