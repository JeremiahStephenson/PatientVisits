package com.jerry.patient.assessment.service

data class Data<out DataType>(
    val status: Status,
    val data: DataType? = null,
    val throwable: Throwable? = null) {

    val isSuccessful get() = status == Status.DONE
    val isLoading get() = status == Status.LOADING
    val isError get() = status == Status.ERROR

    companion object {

        fun <DataType> idle() = Data<DataType>(Status.IDLE)

        fun <DataType> done(data: DataType) = Data(Status.DONE, data)

        fun <DataType> loading(data: DataType? = null) = Data(Status.LOADING, data)

        fun <DataType> error(throwable: Throwable?, data: DataType? = null) =
            Data(Status.ERROR, data, throwable)
    }
}