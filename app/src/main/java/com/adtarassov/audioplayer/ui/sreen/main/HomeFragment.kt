package com.adtarassov.audioplayer.ui.sreen.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.adtarassov.audioplayer.databinding.FragmentHomeBinding
import com.adtarassov.audioplayer.utils.AudioListType
import com.adtarassov.audioplayer.utils.AudioListType.All
import com.adtarassov.audioplayer.utils.AudioListType.FAVORITE

class HomeFragment : Fragment() {

  //todo add viewModel
  private var _binding: FragmentHomeBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentHomeBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.allAudioButton.setOnClickListener {
      navigateToAudioList(All)
    }
    binding.favoriteAudioButton.setOnClickListener {
      navigateToAudioList(FAVORITE)
    }
  }

  private fun navigateToAudioList(listType: AudioListType) {
    val action = HomeFragmentDirections.actionHomeFragmentToAudioListFragment(
      listType = AudioListType.idByType(listType)
    )
    findNavController().navigate(action)
  }

}