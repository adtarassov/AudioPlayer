package com.adtarassov.audioplayer.utils

enum class AudioListType(val id: Int) {
  RECOMMENDATION(0), PROFILE(1), LOCAL(2);

  companion object {
    const val BUNDLE_KEY = "audio_list_type"

    fun typeById(value: Int): AudioListType = when (value) {
      0 -> RECOMMENDATION
      1 -> PROFILE
      2 -> LOCAL
      else -> throw IllegalStateException()
    }
  }
}