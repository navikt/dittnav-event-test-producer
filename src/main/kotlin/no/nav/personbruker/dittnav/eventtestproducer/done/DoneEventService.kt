package no.nav.personbruker.dittnav.eventtestproducer.done

import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.eventtestproducer.common.database.Database
import no.nav.personbruker.dittnav.eventtestproducer.beskjed.getBeskjedByFodselsnummer
import no.nav.personbruker.dittnav.eventtestproducer.innboks.getInnboksByFodselsnummer
import no.nav.personbruker.dittnav.eventtestproducer.oppgave.getOppgaveByFodselsnummer

class DoneEventService(
        private val database: Database) {

    fun markAllBrukernotifikasjonerAsDone(fodselsnummer: String) = runBlocking {
        val beskjed = database.dbQuery { getBeskjedByFodselsnummer(fodselsnummer) }
        val oppgaver = database.dbQuery { getOppgaveByFodselsnummer(fodselsnummer) }
        val innboks = database.dbQuery { getInnboksByFodselsnummer(fodselsnummer) }

        val alleBrukernotifikasjoner = beskjed + oppgaver + innboks

        alleBrukernotifikasjoner.forEach { brukernotifikasjon ->
            DoneProducer.produceDoneEventForSpecifiedEvent(fodselsnummer, brukernotifikasjon)
        }
    }

    fun markEventAsDone(fodselsnummer: String, eventId : String) {
        DoneProducer.produceDoneEventForSuppliedEventId(fodselsnummer, eventId)
    }
}
