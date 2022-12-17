package com.jerry.patient.assessment.inject

import com.jerry.patient.assessment.repository.PatientRepository
import com.jerry.patient.assessment.repository.PatientRepositoryImpl
import com.jerry.patient.assessment.ui.form.FormViewModel
import com.jerry.patient.assessment.ui.home.HomeViewModel
import com.jerry.patient.assessment.util.CoroutineContextProvider
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<PatientRepository> { PatientRepositoryImpl(get(), get()) }

    single<CoroutineContextProvider> { CoroutineContextProvider.MainCoroutineContext }

    viewModel { HomeViewModel(get(), get()) }
    viewModel { FormViewModel(get(), get(), get()) }
}