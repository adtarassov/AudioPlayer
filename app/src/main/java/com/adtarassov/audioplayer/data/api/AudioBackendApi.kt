package com.adtarassov.audioplayer.data.api

import retrofit2.Response
import retrofit2.http.GET

interface AudioBackendApi {

  @GET("posts")
  suspend fun getPosts(): Response<Any>

  companion object {
    const val BASE_URL = "https://jsonplaceholder.typicode.com/"
  }
}