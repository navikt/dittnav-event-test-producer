package no.nav.personbruker.dittnav.eventtestproducer.done

import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.eventtestproducer.common.database.Database
import no.nav.personbruker.dittnav.eventtestproducer.beskjed.getAktivBeskjedByFodselsnummer
import no.nav.personbruker.dittnav.eventtestproducer.common.InnloggetBruker
import no.nav.personbruker.dittnav.eventtestproducer.innboks.getAktivInnboksByFodselsnummer
import no.nav.personbruker.dittnav.eventtestproducer.oppgave.getAktivOppgaveByFodselsnummer

class DoneEventService(
        private val database: Database,
        private val doneProducer: DoneProducer
) {

    fun markAllBrukernotifikasjonerAsDone(innloggetBruker: InnloggetBruker) = runBlocking {
        val beskjed = database.dbQuery { getAktivBeskjedByFodselsnummer(innloggetBruker) }
        val oppgaver = database.dbQuery { getAktivOppgaveByFodselsnummer(innloggetBruker) }
        val innboks = database.dbQuery { getAktivInnboksByFodselsnummer(innloggetBruker) }

        val alleBrukernotifikasjoner = beskjed + oppgaver + innboks

        alleBrukernotifikasjoner.forEach { brukernotifikasjon ->
            doneProducer.produceDoneEventForSpecifiedEvent(innloggetBruker, brukernotifikasjon)
        }
    }

}
