package no.nav.personbruker.dittnav.eventtestproducer.informasjon

import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.eventtestproducer.common.database.H2Database
import org.junit.jupiter.api.Test
import org.amshove.kluent.*

class InformasjonQueriesTest {

    private val database = H2Database()

    @Test
    fun `Finn alle cachede Informasjon-eventer for aktorID`() {
        runBlocking {
            database.dbQuery { getAllInformasjonByAktorId("12345") }.size `should be equal to` 3
        }
    }

    @Test
    fun `Finner kun aktive cachede Informasjon-eventer for aktorID`() {
        runBlocking {
            database.dbQuery { getInformasjonByAktorId("12345") }.size `should be equal to` 2
        }
    }

    @Test
    fun `Returnerer tom liste hvis Informasjon-eventer for aktorID ikke finnes`() {
        runBlocking {
            database.dbQuery { getInformasjonByAktorId("finnesikke") }.`should be empty`()
        }
    }


    @Test
    fun `Returnerer tom liste hvis Informasjon-eventer hvis tom aktorID`() {
        runBlocking {
            database.dbQuery { getInformasjonByAktorId("") }.`should be empty`()
        }
    }
}
