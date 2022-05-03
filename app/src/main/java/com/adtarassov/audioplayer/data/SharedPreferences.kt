package com.adtarassov.audioplayer.data

import android.content.Context
import androidx.preference.PreferenceManager
import com.adtarassov.audioplayer.di.ApplicationScope
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
  private val externalScope: CoroutineScope,
) {
  private val prefs = PreferenceManager.getDefaultSharedPreferences(context)

  private val userAuthModel: MutableStateFlow<UserAuthModel> = MutableStateFlow(
    UserAuthModel(getToken(), getAccountName())
  )
  val userAuthModelFlow: StateFlow<UserAuthModel> = userAuthModel

  fun setToken(token: String?) {
    prefs.edit()
      .putString(TOKEN_KEY, token)
      .apply()

    externalScope.launch {
      val newModel = userAuthModelFlow.value.copy(token = token)
      userAuthModel.emit(newModel)
    }
  }

  fun setAccountName(name: String?) {
    prefs.edit()
      .putString(ACCOUNT_NAME_KEY, name)
      .apply()

    externalScope.launch {
      val newModel = userAuthModelFlow.value.copy(accountName = name)
      userAuthModel.emit(newModel)
    }
  }

  private fun getToken() = prefs.getString(TOKEN_KEY, null)

  private fun getAccountName() = prefs.getString(ACCOUNT_NAME_KEY, null)


  companion object {
    private const val TOKEN_KEY = "token_key"
    private const val ACCOUNT_NAME_KEY = "account_name_key"

    data class UserAuthModel(
      val token: String?,
      val accountName: String?,
    )
  }

}