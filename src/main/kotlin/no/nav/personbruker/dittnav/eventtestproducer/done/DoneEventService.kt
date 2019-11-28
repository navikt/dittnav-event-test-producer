package no.nav.personbruker.dittnav.eventtestproducer.done

import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.eventtestproducer.common.database.Database
import no.nav.personbruker.dittnav.eventtestproducer.beskjed.getBeskjedByAktorId
import no.nav.personbruker.dittnav.eventtestproducer.innboks.getInnboksByAktorId
import no.nav.personbruker.dittnav.eventtestproducer.oppgave.getOppgaveByAktorId

class DoneEventService(
        private val database: Database) {

    fun markAllBrukernotifikasjonerAsDone(aktorId: String) = runBlocking {
        val beskjed = database.dbQuery { getBeskjedByAktorId(aktorId) }
        val oppgaver = database.dbQuery { getOppgaveByAktorId(aktorId) }
        val innboks = database.dbQuery { getInnboksByAktorId(aktorId) }

        val alleBrukernotifikasjoner = beskjed + oppgaver + innboks

        alleBrukernotifikasjoner.forEach { brukernotifikasjon ->
            DoneProducer.produceDoneEventForSpecifiedEvent(aktorId, brukernotifikasjon)
        }
    }

    fun markEventAsDone(aktorId: String, eventId : String) {
        DoneProducer.produceDoneEventForSuppliedEventId(aktorId, eventId)
    }
}
