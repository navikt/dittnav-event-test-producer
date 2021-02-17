package no.nav.personbruker.dittnav.eventtestproducer.ytelsestesting

import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.post
import no.nav.personbruker.dittnav.eventtestproducer.beskjed.ProduceBeskjedDto
import no.nav.personbruker.dittnav.eventtestproducer.common.InnloggetBruker
import no.nav.personbruker.dittnav.eventtestproducer.common.InnloggetBrukerFactory
import no.nav.personbruker.dittnav.eventtestproducer.config.innloggetBruker
import no.nav.personbruker.dittnav.eventtestproducer.config.respondForParameterType

fun Route.ytelsestestApi(testDataService: TestDataService) {

    post("/produce/test/beskjed/done") {
        respondForParameterType<YTestDto> { yTestDto ->
            testDataService.produserBeskjeder(
                    produceDone = true, yTestDto = yTestDto)
            "Har produsert ${yTestDto.antallEventer} Beskjeder, med tilhørende Done-eventer"
        }
    }

    post("/produce/test/beskjed") {
        respondForParameterType<YTestDto> { yTestDto ->
            testDataService.produserBeskjeder(
                    produceDone = false, yTestDto = yTestDto)
            "Har produsert ${yTestDto.antallEventer} Beskjeder, uten Done-eventer"
        }
    }

    post("/produce/test/oppgave/done") {
        respondForParameterType<YTestDto> { yTestDto ->
            testDataService.produserOppgaver(produceDone = true, yTestDto = yTestDto)
            "Har produsert ${yTestDto.antallEventer} Oppgaver, med tilhørende Done-eventer"
        }
    }

    post("/produce/test/oppgave") {
        respondForParameterType<YTestDto> { yTestDto ->
            testDataService.produserOppgaver(produceDone = false, yTestDto = yTestDto)
            "Har produsert ${yTestDto.antallEventer} Oppgaver, uten Done-eventer"
        }
    }

    post("/produce/test/innboks/done") {
        respondForParameterType<YTestDto> { yTestDto ->
            testDataService.produserInnboks(produceDone = true, yTestDto = yTestDto)
            "Har produsert ${yTestDto.antallEventer} Innboks, med tilhørende Done-eventer"
        }
    }

    post("/produce/test/innboks") {
        respondForParameterType<YTestDto> { yTestDto ->
            testDataService.produserInnboks(produceDone = false, yTestDto = yTestDto)
            "Har produsert ${yTestDto.antallEventer} Innboks, uten Done-eventer"
        }
    }

    post("/produce/test/statusoppdatering") {
        respondForParameterType<YTestDto> { yTestDto ->
            testDataService.produserStatusoppdateringer(yTestDto = yTestDto)
            "Har produsert ${yTestDto.antallEventer} Statusoppdateringer"
        }
    }
}
