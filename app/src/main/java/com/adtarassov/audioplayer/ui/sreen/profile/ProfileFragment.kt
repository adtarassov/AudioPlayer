package com.adtarassov.audioplayer.ui.sreen.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.adtarassov.audioplayer.databinding.FragmentProfileBinding
import com.adtarassov.audioplayer.ui.sreen.player.fullscreen.FullScreenPlayerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {

  private var _binding: FragmentProfileBinding? = null
  private val binding get() = _binding!!

  private val viewModel: ProfileViewModel by viewModels()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    _binding = FragmentProfileBinding.inflate(inflater, container, false)
    binding.authButton.setOnClickListener {
      openAuthFragment()
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

  private fun openAuthFragment() {
    val navAction = ProfileFragmentDirections.actionProfileFragmentToAuthorizationFragment()
    findNavController().navigate(navAction)
  }

  private fun bindViewAction(action: ProfileAction) {

  }

  private fun bindViewState(state: ProfileViewState) {
    when (state) {
      is ProfileViewState.Unauthorized -> {
        binding.authorizationView.isVisible = true
      }
    }
  }

}