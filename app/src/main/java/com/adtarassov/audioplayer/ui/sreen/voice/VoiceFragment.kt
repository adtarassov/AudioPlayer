package com.adtarassov.audioplayer.ui.sreen.voice

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.adtarassov.audioplayer.R
import com.adtarassov.audioplayer.databinding.FragmentProfileBinding
import com.adtarassov.audioplayer.databinding.FragmentVoiceBinding
import com.adtarassov.audioplayer.ui.sreen.profile.ProfileAction
import com.adtarassov.audioplayer.ui.sreen.profile.ProfileViewModel
import com.adtarassov.audioplayer.ui.sreen.profile.ProfileViewState
import com.adtarassov.audioplayer.utils.ProfileType
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull

class VoiceFragment : Fragment() {

  private var _binding: FragmentVoiceBinding? = null
  private val binding get() = _binding!!

  private val viewModel: VoiceViewModel by viewModels()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    _binding = FragmentVoiceBinding.inflate(inflater, container, false)
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

  private fun bindViewAction(action: VoiceAction) {

  }

  private fun bindViewState(state: VoiceViewState) {

  }


}