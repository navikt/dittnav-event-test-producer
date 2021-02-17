package no.nav.personbruker.dittnav.eventtestproducer.config

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.ktor.application.*
import io.ktor.auth.Authentication
import io.ktor.auth.authenticate
import io.ktor.auth.authentication
import io.ktor.features.CORS
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.http.HttpHeaders
import io.ktor.jackson.jackson
import io.ktor.routing.routing
import io.ktor.util.KtorExperimentalAPI
import io.ktor.util.pipeline.PipelineContext
import io.prometheus.client.hotspot.DefaultExports
import no.nav.personbruker.dittnav.eventtestproducer.beskjed.beskjedApi
import no.nav.personbruker.dittnav.eventtestproducer.common.InnloggetBruker
import no.nav.personbruker.dittnav.eventtestproducer.common.InnloggetBrukerFactory
import no.nav.personbruker.dittnav.eventtestproducer.common.healthApi
import no.nav.personbruker.dittnav.eventtestproducer.done.doneApi
import no.nav.personbruker.dittnav.eventtestproducer.innboks.innboksApi
import no.nav.personbruker.dittnav.eventtestproducer.oppgave.oppgaveApi
import no.nav.personbruker.dittnav.eventtestproducer.statusoppdatering.statusoppdateringApi
import no.nav.personbruker.dittnav.eventtestproducer.ytelsestesting.ytelsestestApi
import no.nav.security.token.support.ktor.tokenValidationSupport

@KtorExperimentalAPI
fun Application.mainModule(appContext: ApplicationContext = ApplicationContext()) {
    DefaultExports.initialize()
    install(DefaultHeaders)

    install(CORS) {
        host(appContext.environment.corsAllowedOrigins, schemes = listOf("https"))
        allowCredentials = true
        header(HttpHeaders.ContentType)
    }

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
            oppgaveApi(appContext.oppgaveProducer)
            beskjedApi(appContext.beskjedProducer)
            innboksApi(appContext.innboksProducer)
            doneApi(appContext.doneEventService)
            statusoppdateringApi(appContext.statusoppdateringProducer)
            ytelsestestApi(appContext.testDataService)
        }
    }

    configureShutdownHook(appContext)
}

val PipelineContext<Unit, ApplicationCall>.innloggetBruker: InnloggetBruker
    get() = InnloggetBrukerFactory.createNewInnloggetBruker(call.authentication.principal())

private fun Application.configureShutdownHook(appContext: ApplicationContext) {
    environment.monitor.subscribe(ApplicationStopPreparing) {
        appContext.database.dataSource.close()
        appContext.kafkaProducerBeskjed.flushAndClose()
        appContext.kafkaProducerDone.flushAndClose()
        appContext.kafkaProducerInnboks.flushAndClose()
        appContext.kafkaProducerOppgave.flushAndClose()
        appContext.kafkaProducerStatusoppdatering.flushAndClose()
    }
}
