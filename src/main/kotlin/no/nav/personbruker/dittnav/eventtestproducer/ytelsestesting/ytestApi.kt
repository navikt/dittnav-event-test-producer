package no.nav.personbruker.dittnav.eventtestproducer.ytelsestesting

import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.post

fun Route.ytelsestestApi(testDataService: TestDataService) {

    post("/produce/testeventer") {
        testDataService.produserTestCase()

        call.respondText(text = "Har produsert test-eventer", contentType = ContentType.Text.Plain)
    }

}
