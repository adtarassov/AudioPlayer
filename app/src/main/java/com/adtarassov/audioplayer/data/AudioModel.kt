package com.adtarassov.audioplayer.data

data class AudioModel(
  val title: String,
  val artist: String?,
  val album: String?,
  val durationMs: Long,
  val filePath: String
)
