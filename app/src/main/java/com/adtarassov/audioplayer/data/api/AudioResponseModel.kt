package com.adtarassov.audioplayer.data.api

import com.google.gson.annotations.SerializedName

data class AudioResponseModel(
  @SerializedName("account_name")
  val accountName: String,
  @SerializedName("id")
  val audioId: Long,
  @SerializedName("description")
  val description: String?,
  @SerializedName("name")
  val name: String,
  @SerializedName("file")
  val audioUrl: String,
)


