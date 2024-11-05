package com.nezuko.domain.model

class ResultModel<out T> (
    val status: Status,
    val data: T?,
    val message: String?
) {

    enum class Status {
        SUCCESS,
        FAILURE,
        LOADING,
        NONE
    }

    companion object {

        fun <T> success(data: T): ResultModel<T> = ResultModel(Status.SUCCESS, data, null)

        fun <T> failure(message: String?, data: T? = null): ResultModel<T> = ResultModel(Status.FAILURE, data, message)

        fun <T> loading(): ResultModel<T> = ResultModel(Status.LOADING, null, null)

        fun <T> none(): ResultModel<T> = ResultModel(Status.NONE, null, null)

    }

    override fun toString(): String {
        return "data = $data, status = $status"
    }
}