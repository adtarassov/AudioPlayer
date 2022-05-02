package com.adtarassov.audioplayer.utils

enum class ProfileType(val id: Int) {
  MAIN(0), OTHER(1);

  companion object {
    const val BUNDLE_KEY = "profile_type"

    fun typeById(value: Int): ProfileType = when (value) {
      0 -> MAIN
      1 -> OTHER
      else -> throw IllegalStateException()
    }
  }
}