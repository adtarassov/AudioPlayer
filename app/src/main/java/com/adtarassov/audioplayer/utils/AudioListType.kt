package com.adtarassov.audioplayer.utils

enum class AudioListType{
  All, FAVORITE;

  companion object {
    const val BUNDLE_KEY = "list_type"
    
    fun typeById(value: Int): AudioListType  = when (value) {
      0 -> All
      1 -> FAVORITE
      else -> throw IllegalStateException()
    }
    
    fun idByType(type: AudioListType): Int  = when (type) {
      All -> 0
      FAVORITE -> 1
    }

    fun getToolbarSubtitleByType(type: AudioListType): String  = when (type) {
      All -> "Все"
      FAVORITE -> "Избранные"
    }
  }
}