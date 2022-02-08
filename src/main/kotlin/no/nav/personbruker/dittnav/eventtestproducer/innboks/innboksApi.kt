package no.nav.personbruker.dittnav.eventtestproducer.innboks

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import no.nav.personbruker.dittnav.eventtestproducer.config.innloggetBruker
import no.nav.personbruker.dittnav.eventtestproducer.config.respondForParameterType

fun Route.innboksApi(innboksProducer: InnboksProducer) {

    post("/produce/innboks") {
        this.respondForParameterType<ProduceInnboksDto> { innboksDto ->
            innboksProducer.produceInnboksEventForIdent(innloggetBruker, innboksDto)
            "Et innboks-event for for brukeren: $innloggetBruker har blitt lagt på kafka."
        }
    }

    get("/produce/test/innboks") {
        val innboksDto = createTestEventMedEksternVarslingAktivert()
        innboksProducer.produceInnboksEventForIdent(innloggetBruker, innboksDto)
        val message =
            "Innboks med ekstern-varsling aktivert har blitt produsert, med følgende tekst: '${innboksDto.tekst}'."
        call.respondText(text = message, contentType = ContentType.Text.Plain)
    }
}


private fun createTestEventMedEksternVarslingAktivert(): ProduceInnboksDto {
    val tekst = "Dette er et test-innboks-event med ekstern varsling og en default-tekst."
    val link = "https://www.nav.no"
    val grupperingsid = "1234"
    return ProduceInnboksDto(
        tekst = tekst,
        link = link,
        grupperingsid = grupperingsid,
        eksternVarsling = true)
}
