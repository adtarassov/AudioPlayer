package com.adtarassov.audioplayer.data.api

import com.google.gson.annotations.SerializedName

data class TokenModel(
  @SerializedName("refresh")
  val tokenRefresh: String?,

  @SerializedName("access")
  val tokenAccess: String?,
)
