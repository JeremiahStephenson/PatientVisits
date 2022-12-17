package com.jerry.patient.assessment.inject

import com.jerry.patient.assessment.service.VisitsAPI
import com.jerry.patient.assessment.service.adapters.LocalDateAdapter
import com.jerry.patient.assessment.service.adapters.LocalDateTimeAdapter
import com.jerry.patient.assessment.service.adapters.ResourceAdapter
import com.squareup.moshi.Moshi
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val apiModule = module {

    single {
        Moshi.Builder()
            .add(LocalDateAdapter())
            .add(LocalDateTimeAdapter())
            .add(ResourceAdapter())
            .build()
    }

    single { MoshiConverterFactory.create(get()).asLenient() }

    single {
        // The json from a remote file in order to simulate a server response
        Retrofit.Builder()
            .addConverterFactory(get<MoshiConverterFactory>())
            .baseUrl("https://raw.githubusercontent.com/JeremiahStephenson/Files/master/")
            .build()
    }
    single { get<Retrofit>().create(VisitsAPI::class.java) }
}