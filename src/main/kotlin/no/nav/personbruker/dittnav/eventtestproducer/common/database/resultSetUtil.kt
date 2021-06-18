package no.nav.personbruker.dittnav.eventtestproducer.common.database

import java.sql.ResultSet
import java.sql.Timestamp
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

fun <T> ResultSet.map(result: ResultSet.() -> T): List<T> =
        mutableListOf<T>().apply {
            while (next()) {
                add(result())
            }
        }

fun ResultSet.getUtcTimeStamp(label: String): Timestamp = getTimestamp(label, Calendar.getInstance(TimeZone.getTimeZone("UTC")))

fun ResultSet.getNullableUtcTimeStamp(label: String): Timestamp? = getTimestamp(label, Calendar.getInstance())

fun ResultSet.getNullableZonedDateTime(label: String) : ZonedDateTime? {
    return getNullableUtcTimeStamp(label)?.let { timestamp -> ZonedDateTime.ofInstant(timestamp.toInstant(), ZoneId.of("Europe/Oslo")) }
}

fun ResultSet.getListFromSeparatedString(columnLabel: String, separator: String): List<String> {
    var stringValue = getString(columnLabel)
    return if(stringValue.isNullOrEmpty()) {
        emptyList()
    }
    else {
        stringValue.split(separator)
    }
}
