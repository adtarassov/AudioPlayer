package com.adtarassov.audioplayer.data

import android.content.Context
import androidx.preference.PreferenceManager
import com.adtarassov.audioplayer.di.ApplicationScope
import com.adtarassov.audioplayer.utils.player.AudioService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferences @Inject constructor(
  @ApplicationContext
  context: Context,
  @ApplicationScope
  private val externalScope: CoroutineScope
) {
  private val prefs = PreferenceManager.getDefaultSharedPreferences(context)

  private val userToken: MutableStateFlow<String?> = MutableStateFlow(getToken())
  val userTokenFlow: StateFlow<String?> = userToken

  fun setToken(token: String?) {
    prefs.edit()
      .putString(TOKEN_KEY, token)
      .apply()

    externalScope.launch {
      userToken.emit(token)
    }
  }

  private fun getToken() = prefs.getString(TOKEN_KEY, null)

  companion object {
    private const val TOKEN_KEY = "token_key"
  }

}