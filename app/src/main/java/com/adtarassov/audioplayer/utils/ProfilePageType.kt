package com.adtarassov.audioplayer.utils

enum class ProfilePageType(val id: Int) {
  MAIN(0), OTHER(1);

  companion object {
    const val BUNDLE_KEY = "profile_type"
    const val USER_ACCOUNT_NAME_KEY = "user_account_name_key"

    fun typeById(value: Int): ProfilePageType = when (value) {
      0 -> MAIN
      1 -> OTHER
      else -> throw IllegalStateException()
    }
  }
}