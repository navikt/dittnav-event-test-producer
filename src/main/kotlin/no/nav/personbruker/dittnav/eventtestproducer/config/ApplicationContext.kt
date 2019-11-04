package no.nav.personbruker.dittnav.eventtestproducer.config

import no.nav.personbruker.dittnav.eventtestproducer.common.database.Database
import no.nav.personbruker.dittnav.eventtestproducer.common.database.PostgresDatabase
import no.nav.personbruker.dittnav.eventtestproducer.done.DoneEventService

class ApplicationContext {

    val environment = Environment()
    val database: Database = PostgresDatabase(environment)
    val doneEventService = DoneEventService(database)

}
