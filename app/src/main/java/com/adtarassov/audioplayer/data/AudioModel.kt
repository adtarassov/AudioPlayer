package com.adtarassov.audioplayer.data

data class AudioModel(
  val author: String,
  val title: String,
  val subtitle: String?,
  val durationMs: Long,
  var isLiked: Boolean,
  val filePath: String
)