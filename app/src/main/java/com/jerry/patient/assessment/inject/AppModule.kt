package com.jerry.patient.assessment.inject

import com.jerry.patient.assessment.form.FormViewModel
import com.jerry.patient.assessment.home.HomeViewModel
import com.jerry.patient.assessment.repository.PatientRepository
import com.jerry.patient.assessment.repository.PatientRepositoryImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<PatientRepository> { PatientRepositoryImpl(get()) }

    viewModel { HomeViewModel(get(), get()) }
    viewModel { FormViewModel(get()) }
}