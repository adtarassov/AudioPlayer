package com.adtarassov.audioplayer.utils

data class Event<out T>(val status: Status, val data: T?, val message: String?) {

  enum class Status {
    SUCCESS,
    ERROR,
    LOADING
  }

  companion object {
    fun <T> success(data: T? = null): Event<T> {
      return Event(Status.SUCCESS, data, null)
    }

    fun <T> error(message: String, data: T? = null): Event<T> {
      return Event(Status.ERROR, data, message)
    }

    fun <T> loading(): Event<T> {
      return Event(Status.LOADING, null, null)
    }
  }
}