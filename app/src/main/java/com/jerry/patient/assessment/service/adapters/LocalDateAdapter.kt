package com.jerry.patient.assessment.service.adapters

import com.jerry.patient.assessment.core.LOCAL_DATE_UTC_PARSER
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

class LocalDateAdapter {
    @ToJson
    fun toJson(localDate: LocalDate): String =
            localDate.format(LOCAL_DATE_UTC_PARSER)

    @FromJson
    fun fromJson(json: String): LocalDate =
            LocalDate.parse(json, LOCAL_DATE_UTC_PARSER)
}