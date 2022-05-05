package com.adtarassov.audioplayer.ui.sreen.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.adtarassov.audioplayer.databinding.FragmentProfileBinding
import com.adtarassov.audioplayer.ui.sreen.audiolist.AudioListFragment
import com.adtarassov.audioplayer.ui.sreen.authorization.AuthorizationFragment
import com.adtarassov.audioplayer.utils.AudioListType
import com.adtarassov.audioplayer.utils.ProfilePageType
import com.adtarassov.audioplayer.utils.ProfilePageType.Companion.USER_ACCOUNT_NAME_KEY
import com.adtarassov.audioplayer.utils.ProfilePageType.MAIN
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull

@AndroidEntryPoint
class ProfileFragment : Fragment() {

  private var _binding: FragmentProfileBinding? = null
  private val binding get() = _binding!!
  private lateinit var profilePageType: ProfilePageType

  private val viewModel: ProfileViewModel by viewModels()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    _binding = FragmentProfileBinding.inflate(inflater, container, false)
    profilePageType = ProfilePageType.typeById(arguments?.getInt(ProfilePageType.BUNDLE_KEY) ?: MAIN.id) // нужно для разного UI
    binding.authButton.setOnClickListener {
      openAuthFragment(AuthorizationFragment.Companion.AuthType.AUTHORIZATION)
    }
    binding.registrationButton.setOnClickListener {
      openAuthFragment(AuthorizationFragment.Companion.AuthType.REGISTRATION)
    }
    binding.exitButton.setOnClickListener {
      viewModel.obtainEvent(ProfileEvent.OnExitButtonClicked)
    }
    viewModel.obtainEvent(ProfileEvent.ViewCreate(profilePageType))
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

  private fun openAuthFragment(type: AuthorizationFragment.Companion.AuthType) {
    val navAction = ProfileFragmentDirections.actionProfileFragmentToAuthorizationFragment(
      authType = type.id
    )
    findNavController().navigate(navAction)
  }

  private fun bindViewAction(action: ProfileAction) {

  }

  private fun bindAudioListFragment(userAccountName: String) {
    val bundle = bundleOf(
      AudioListType.BUNDLE_KEY to AudioListType.PROFILE.id,
      USER_ACCOUNT_NAME_KEY to userAccountName
    )
    parentFragmentManager.commit {
      setReorderingAllowed(true)
      replace<AudioListFragment>(
        binding.audioListFragmentContainer.id,
        args = bundle,
        tag = USER_AUDIO_LIST_FRAGMENT_TAG
      )
    }
    binding.audioListFragmentContainer.isVisible = true
  }


  private fun bindViewState(state: ProfileViewState) {
    when (state) {
      is ProfileViewState.Unauthorized -> {
        binding.authorizationView.isVisible = true
        binding.progressView.isVisible = false

        val prevFragment = parentFragmentManager.findFragmentByTag(USER_AUDIO_LIST_FRAGMENT_TAG)
        binding.audioListFragmentContainer.isVisible = false
        prevFragment?.let {
          parentFragmentManager.commit {
            remove(it)
          }
        }
        binding.dividerView.isVisible = false
        binding.userAvatar.isVisible = false
        binding.usernameTv.isVisible = false
        binding.usernameDescriptionTv.isVisible = false
        binding.exitButton.isVisible = false
      }
      is ProfileViewState.Loading -> {
        binding.authorizationView.isVisible = false
        binding.progressView.isVisible = true

        binding.audioListFragmentContainer.isVisible = false
        binding.dividerView.isVisible = false
        binding.userAvatar.isVisible = false
        binding.usernameTv.isVisible = false
        binding.usernameDescriptionTv.isVisible = false
        binding.exitButton.isVisible = false
      }
      is ProfileViewState.Loaded -> {
        binding.authorizationView.isVisible = false
        binding.progressView.isVisible = false

        binding.userAvatar.isVisible = true
        binding.dividerView.isVisible = true
        binding.usernameTv.isVisible = true
        binding.usernameDescriptionTv.isVisible = true
        binding.exitButton.isVisible = true

        binding.usernameTv.text = state.user.username
        binding.usernameDescriptionTv.text = state.user.description
        bindAudioListFragment(state.user.username)
      }
    }
  }

  companion object {
    const val USER_AUDIO_LIST_FRAGMENT_TAG = "user_audio_list"
  }

}