package no.nav.personbruker.dittnav.eventtestproducer.config

object ConfigUtil {

    fun isCurrentlyRunningOnNais(): Boolean {
        return System.getenv("NAIS_APP_NAME") != null
    }
}