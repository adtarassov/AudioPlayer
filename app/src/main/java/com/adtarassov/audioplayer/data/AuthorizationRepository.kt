package com.adtarassov.audioplayer.data

import com.adtarassov.audioplayer.data.api.AudioBackendApi
import kotlinx.coroutines.delay
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthorizationRepository @Inject constructor(
  private val api: AudioBackendApi
) {
  suspend fun authUser(): Response<Any> {
    delay(1000)
    return api.getPosts()
  }
}