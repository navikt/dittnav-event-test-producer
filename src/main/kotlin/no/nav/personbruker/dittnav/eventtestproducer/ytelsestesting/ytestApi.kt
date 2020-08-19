package no.nav.personbruker.dittnav.eventtestproducer.ytelsestesting

import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.post

fun Route.ytelsestestApi(testDataService: TestDataService) {

    post("/produce/test/beskjed") {
        testDataService.produserBeskjederOgTilhorendeDoneEventer()

        call.respondText(text = "Har produsert beskjed-test-eventer", contentType = ContentType.Text.Plain)
    }

    post("/produce/test/oppgave") {
        testDataService.produserOppgaveOgTilhorendeDoneEventer()

        call.respondText(text = "Har produsert oppgave-test-eventer", contentType = ContentType.Text.Plain)
    }

    post("/produce/test/innboks") {
        testDataService.produserInnboksOgTilhorendeDoneEventer()

        call.respondText(text = "Har produsert innboks-test-eventer", contentType = ContentType.Text.Plain)
    }

    post("/produce/test/statusoppdatering") {
        testDataService.produserStatusOppdateringsEventer()

        call.respondText(text = "Har produsert statusoppdatering-test-eventer", contentType = ContentType.Text.Plain)
    }

}
