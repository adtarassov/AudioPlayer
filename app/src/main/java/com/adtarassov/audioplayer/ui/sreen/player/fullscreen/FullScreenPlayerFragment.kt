package com.adtarassov.audioplayer.ui.sreen.player.fullscreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.adtarassov.audioplayer.R
import com.adtarassov.audioplayer.databinding.FragmentFullScreenPlayerBinding
import com.adtarassov.audioplayer.databinding.FragmentSmallPlayerBinding
import com.adtarassov.audioplayer.ui.sreen.player.smallscreen.SmallPlayerViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FullScreenPlayerFragment : BottomSheetDialogFragment() {

  private val viewModel: FullScreenPlayerViewModel by viewModels()

  private var _binding: FragmentFullScreenPlayerBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    _binding = FragmentFullScreenPlayerBinding.inflate(inflater, container, false)
    return binding.root
  }

}