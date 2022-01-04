package com.adtarassov.audioplayer.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.adtarassov.audioplayer.databinding.FragmentAudioListBinding
import com.adtarassov.audioplayer.utils.AudioListType

class AudioListFragment : Fragment() {

  private val viewModel: AudioListViewModel by viewModels()
  private val args:  AudioListFragmentArgs by navArgs()
  private var _binding: FragmentAudioListBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentAudioListBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val listType = AudioListType.typeById(args.listType)
  }

}