package com.jerry.patient.assessment

import com.jerry.patient.assessment.service.adapters.LocalDateAdapter
import com.jerry.patient.assessment.service.adapters.LocalDateTimeAdapter
import com.jerry.patient.assessment.service.adapters.ResourceAdapter
import com.jerry.patient.assessment.service.data.*
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import okio.buffer
import okio.source
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class ResourceAdapterParseTest {

    @get:Rule
    val testRule = MainTestRule()

    @Test
    fun testJsonParse() {
        val sampleJson = javaClass.classLoader!!.getResourceAsStream("patient-feedback-raw-data.json")!!
        val type = Types.newParameterizedType(List::class.java, ResourceDto::class.java)
        val moshi = Moshi.Builder()
            .add(LocalDateTimeAdapter())
            .add(LocalDateAdapter())
            .add(ResourceAdapter()).build()

        val jsonAdapter: JsonAdapter<List<ResourceDto>> = moshi.adapter(type)
        val mockJson = jsonAdapter.fromJson(sampleJson.source().buffer())!!

        Assert.assertEquals(4, mockJson.size)
        Assert.assertTrue(mockJson[0].resource is PatientDto)
        Assert.assertTrue(mockJson[1].resource is DoctorDto)
        Assert.assertTrue(mockJson[2].resource is AppointmentDto)
        Assert.assertTrue(mockJson[3].resource is DiagnosisDto)
    }
}