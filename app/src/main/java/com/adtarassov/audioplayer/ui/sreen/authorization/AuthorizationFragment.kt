package com.adtarassov.audioplayer.ui.sreen.authorization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.adtarassov.audioplayer.R
import com.adtarassov.audioplayer.databinding.FragmentAuthorizationBinding
import com.adtarassov.audioplayer.ui.sreen.authorization.AuthorizationEvent.OnLoginButtonClicked
import com.adtarassov.audioplayer.ui.sreen.authorization.AuthorizationFragment.Companion.AuthType.AUTHORIZATION
import com.adtarassov.audioplayer.ui.sreen.authorization.AuthorizationFragment.Companion.AuthType.REGISTRATION
import com.adtarassov.audioplayer.utils.extentions.setInvisible
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull

@AndroidEntryPoint
class AuthorizationFragment : BottomSheetDialogFragment() {

  private val viewModel: AuthorizationViewModel by viewModels()
  private val args: AuthorizationFragmentArgs by navArgs()
  private lateinit var authType: AuthType
  private var _binding: FragmentAuthorizationBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    _binding = FragmentAuthorizationBinding.inflate(inflater, container, false)
    authType = authTypeById(args.authType)
    when (authType) {
      AUTHORIZATION -> binding.title.text = getText(R.string.authorize)
      REGISTRATION -> binding.title.text = getText(R.string.authorize_registration)
    }
    binding.authButton.setOnClickListener {
      val username = binding.usernameEt.text.toString()
      val password = binding.passwordEt.text.toString()
      viewModel.obtainEvent(
        OnLoginButtonClicked(username, password, authType)
      )
    }
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    lifecycleScope.launchWhenCreated {
      viewModel.viewStates().filterNotNull().collect { state -> bindViewState(state) }
    }
    lifecycleScope.launchWhenCreated {
      viewModel.viewActions().filterNotNull().collect { action -> bindViewAction(action) }
    }
  }

  private fun authTypeById(id: Int): AuthType = when (id) {
    0 -> AUTHORIZATION
    1 -> REGISTRATION
    else -> AUTHORIZATION
  }

  private fun bindViewState(state: AuthorizationViewState) {
    when (state) {
      is AuthorizationViewState.Loading -> {
        binding.authView.setInvisible()
        binding.progressView.isVisible = true
      }
      is AuthorizationViewState.Error -> {
        binding.authView.isVisible = true
        binding.errorTv.isVisible = true
        binding.errorTv.text = state.error
        binding.progressView.isVisible = false
      }
    }
  }

  private fun bindViewAction(action: AuthorizationAction) {
    when (action) {
      is AuthorizationAction.AuthorizationSuccess -> {
        dismiss()
      }
    }
  }

  companion object {
    enum class AuthType(val id: Int) {
      AUTHORIZATION(0),
      REGISTRATION(1)
    }
  }

}