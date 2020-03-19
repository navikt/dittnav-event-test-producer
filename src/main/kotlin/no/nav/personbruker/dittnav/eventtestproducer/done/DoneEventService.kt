package no.nav.personbruker.dittnav.eventtestproducer.done

import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.eventtestproducer.beskjed.getBeskjedByFodselsnummer
import no.nav.personbruker.dittnav.eventtestproducer.common.InnloggetBruker
import no.nav.personbruker.dittnav.eventtestproducer.common.database.Database
import no.nav.personbruker.dittnav.eventtestproducer.innboks.getInnboksByFodselsnummer
import no.nav.personbruker.dittnav.eventtestproducer.oppgave.getOppgaveByFodselsnummer

class DoneEventService(
        private val database: Database,
        private val doneProducer: DoneProducer
) {

    fun markAllBrukernotifikasjonerAsDone(innloggetBruker: InnloggetBruker) = runBlocking {
        val beskjed = database.dbQuery { getBeskjedByFodselsnummer(innloggetBruker) }
        val oppgaver = database.dbQuery { getOppgaveByFodselsnummer(innloggetBruker) }
        val innboks = database.dbQuery { getInnboksByFodselsnummer(innloggetBruker) }

        val alleBrukernotifikasjoner = beskjed + oppgaver + innboks

        alleBrukernotifikasjoner.forEach { brukernotifikasjon ->
            doneProducer.produceDoneEventForSpecifiedEvent(innloggetBruker, brukernotifikasjon)
        }
    }

}
