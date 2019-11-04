package no.nav.personbruker.dittnav.eventtestproducer.config

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.authenticate
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.jackson.jackson
import io.ktor.routing.routing
import io.ktor.util.KtorExperimentalAPI
import io.prometheus.client.hotspot.DefaultExports
import no.nav.personbruker.dittnav.eventtestproducer.common.healthApi
import no.nav.personbruker.dittnav.eventtestproducer.done.doneApi
import no.nav.personbruker.dittnav.eventtestproducer.informasjon.informasjonApi
import no.nav.personbruker.dittnav.eventtestproducer.oppgave.oppgaveApi
import no.nav.security.token.support.ktor.tokenValidationSupport

@KtorExperimentalAPI
fun Application.mainModule(appContext: ApplicationContext = ApplicationContext()) {
    DefaultExports.initialize()
    install(DefaultHeaders)

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
            registerModule(JavaTimeModule())
        }
    }

    val config = this.environment.config

    install(Authentication) {
        tokenValidationSupport(config = config)
    }

    routing {
        healthApi()
        authenticate {
            oppgaveApi()
            informasjonApi()
            doneApi(appContext.doneEventService)
        }
    }

}
