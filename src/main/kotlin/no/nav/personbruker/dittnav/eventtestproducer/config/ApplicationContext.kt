package no.nav.personbruker.dittnav.eventtestproducer.config

import no.nav.personbruker.dittnav.eventtestproducer.beskjed.BeskjedProducer
import no.nav.personbruker.dittnav.eventtestproducer.common.database.Database
import no.nav.personbruker.dittnav.eventtestproducer.common.database.PostgresDatabase
import no.nav.personbruker.dittnav.eventtestproducer.done.DoneEventService
import no.nav.personbruker.dittnav.eventtestproducer.done.DoneProducer
import no.nav.personbruker.dittnav.eventtestproducer.innboks.InnboksProducer
import no.nav.personbruker.dittnav.eventtestproducer.oppgave.OppgaveProducer
import no.nav.personbruker.dittnav.eventtestproducer.statusOppdatering.StatusOppdateringProducer
import no.nav.personbruker.dittnav.eventtestproducer.ytelsestesting.TestDataService

class ApplicationContext {

    val environment = Environment()
    val database: Database = PostgresDatabase(environment)
    val beskjedProducer = BeskjedProducer(environment)
    val oppgaveProducer = OppgaveProducer(environment)
    val innboksProducer = InnboksProducer(environment)
    val doneProducer = DoneProducer(environment)
    val statusOppdateringProducer = StatusOppdateringProducer(environment)
    val doneEventService = DoneEventService(database, doneProducer)
    val testDataService = TestDataService(doneProducer, beskjedProducer, oppgaveProducer, innboksProducer, statusOppdateringProducer)

}
