package no.nav.personbruker.dittnav.eventtestproducer.common.kafka

data class RecordKeyValueWrapper <K, V> (
    val key: K,
    val value: V
)
