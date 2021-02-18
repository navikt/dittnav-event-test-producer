package no.nav.personbruker.dittnav.eventtestproducer.beskjed

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import no.nav.personbruker.dittnav.eventtestproducer.config.innloggetBruker

fun Route.beskjedApi(beskjedProducer: BeskjedProducer) {

    get("/produce/test/beskjed") {
        val beskjedDto = createTestEventMedEksternVarslingAktivert()
//        beskjedProducer.produceBeskjedEventForIdent(innloggetBruker, beskjedDto)
//        var message = "Beskjed med ekstern-varsling aktivet har blitt produsert, med følgende tekst: '${beskjedDto.tekst}'."
        val message = "Dette er foreløpig kun dry-run. Men dette skal etterhvert produsere en beskjed."
        call.respondText(text = message, contentType = ContentType.Text.Plain)
    }

}

private fun createTestEventMedEksternVarslingAktivert(): ProduceBeskjedDto {
    val tekst = "Dette er en test-beskjed for å sjekke at ekstern-varsling fungerer."
    val link = "https://tjenester.nav.no/saksoversikt"
    val grupperingsid = "123"
    return ProduceBeskjedDto(tekst, link, grupperingsid, true)
}
