package no.nav.personbruker.dittnav.eventtestproducer.config

import no.nav.brukernotifikasjon.schemas.*
import no.nav.personbruker.dittnav.common.util.kafka.producer.KafkaProducerWrapper
import no.nav.personbruker.dittnav.eventtestproducer.beskjed.BeskjedProducer
import no.nav.personbruker.dittnav.eventtestproducer.common.database.Database
import no.nav.personbruker.dittnav.eventtestproducer.common.database.PostgresDatabase
import no.nav.personbruker.dittnav.eventtestproducer.done.DoneEventService
import no.nav.personbruker.dittnav.eventtestproducer.done.DoneProducer
import no.nav.personbruker.dittnav.eventtestproducer.innboks.InnboksProducer
import no.nav.personbruker.dittnav.eventtestproducer.oppgave.OppgaveProducer
import no.nav.personbruker.dittnav.eventtestproducer.statusoppdatering.StatusoppdateringProducer
import no.nav.personbruker.dittnav.eventtestproducer.ytelsestesting.TestDataService
import org.apache.kafka.clients.producer.KafkaProducer

class ApplicationContext {

    val environment = Environment()
    val database: Database = PostgresDatabase(environment)

    val kafkaProducerBeskjed = KafkaProducerWrapper(Kafka.beskjedTopicName, KafkaProducer<Nokkel, Beskjed>(Kafka.producerProps(environment, EventType.BESKJED)))
    val beskjedProducer = BeskjedProducer(kafkaProducerBeskjed, environment.systemUserName)

    val kafkaProducerOppgave = KafkaProducerWrapper(Kafka.oppgaveTopicName, KafkaProducer<Nokkel, Oppgave>(Kafka.producerProps(environment, EventType.OPPGAVE)))
    val oppgaveProducer = OppgaveProducer(kafkaProducerOppgave, environment.systemUserName)

    val kafkaProducerInnboks = KafkaProducerWrapper(Kafka.innboksTopicName, KafkaProducer<Nokkel, Innboks>(Kafka.producerProps(environment, EventType.INNBOKS)))
    val innboksProducer = InnboksProducer(kafkaProducerInnboks, environment.systemUserName)

    val kafkaProducerDone = KafkaProducerWrapper(Kafka.doneTopicName, KafkaProducer<Nokkel, Done>(Kafka.producerProps(environment, EventType.DONE)))
    val doneProducer = DoneProducer(kafkaProducerDone, environment.systemUserName)

    val kafkaProducerStatusoppdatering = KafkaProducerWrapper(Kafka.statusoppdateringTopicName, KafkaProducer<Nokkel, Statusoppdatering>(Kafka.producerProps(environment, EventType.STATUSOPPDATERING)))
    val statusoppdateringProducer = StatusoppdateringProducer(kafkaProducerStatusoppdatering, environment.systemUserName)

    val doneEventService = DoneEventService(database, doneProducer)
    val testDataService = TestDataService(doneProducer, beskjedProducer, oppgaveProducer, innboksProducer, statusoppdateringProducer)

}
