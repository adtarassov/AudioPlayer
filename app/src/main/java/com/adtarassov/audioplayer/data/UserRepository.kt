package com.adtarassov.audioplayer.data

import com.adtarassov.audioplayer.data.api.AudioBackendApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
  private val api: AudioBackendApi
) {
  suspend fun authorizeUser(username: String, password: String) = api.postAuthorizeUser(username, password)

  suspend fun registerUser(username: String, password: String) = api.postRegisterUser(username, password)
}