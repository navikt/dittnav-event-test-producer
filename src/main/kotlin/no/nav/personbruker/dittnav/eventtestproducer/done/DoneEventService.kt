package no.nav.personbruker.dittnav.eventtestproducer.done

import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.eventtestproducer.common.database.Brukernotifikasjon
import no.nav.personbruker.dittnav.eventtestproducer.common.database.Database
import no.nav.personbruker.dittnav.eventtestproducer.informasjon.getInformasjonByAktorId
import no.nav.personbruker.dittnav.eventtestproducer.oppgave.getOppgaveByAktorId

class DoneEventService(
        private val database: Database) {

    fun markAllBrukernotifikasjonerAsDone(aktorId: String) = runBlocking {
        val alleBrukernotifikasjoner = mutableListOf<Brukernotifikasjon>()
        val informasjon = database.dbQuery { getInformasjonByAktorId(aktorId) }
        val oppgaver = database.dbQuery { getOppgaveByAktorId(aktorId) }

        alleBrukernotifikasjoner.addAll(informasjon)
        alleBrukernotifikasjoner.addAll(oppgaver)

        alleBrukernotifikasjoner.forEach { brukernotifikasjon ->
            DoneProducer.produceDoneEventForIdent(aktorId, brukernotifikasjon)
        }
    }

    fun markEventAsDone(aktorId: String, eventId : String) {
        DoneProducer.produceDoneEventForIdent(aktorId, eventId)
    }

}
