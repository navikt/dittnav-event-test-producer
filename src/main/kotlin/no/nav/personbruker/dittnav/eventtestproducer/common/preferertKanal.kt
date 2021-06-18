package no.nav.personbruker.dittnav.eventtestproducer.common

import no.nav.brukernotifikasjon.schemas.builders.domain.PreferertKanal

fun getPrefererteKanaler(prefererteKanaler: List<String>): List<PreferertKanal> {
    return prefererteKanaler.map { preferertKanal -> PreferertKanal.valueOf(preferertKanal) }
}
