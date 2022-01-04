package com.adtarassov.audioplayer.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.adtarassov.audioplayer.databinding.FragmentAudioListBinding
import com.adtarassov.audioplayer.utils.AudioListType
import com.adtarassov.audioplayer.utils.Event.Status.ERROR
import com.adtarassov.audioplayer.utils.Event.Status.LOADING
import com.adtarassov.audioplayer.utils.Event.Status.SUCCESS
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AudioListFragment : Fragment() {

  private val viewModel: AudioListViewModel by viewModels()
  private val args:  AudioListFragmentArgs by navArgs()
  private lateinit var audioListType: AudioListType
  private lateinit var adapter: AudioListAdapter
  private var _binding: FragmentAudioListBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentAudioListBinding.inflate(inflater, container, false)
    audioListType = AudioListType.typeById(args.listType)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    adapter = AudioListAdapter(viewModel::onAudioClick)
    binding.audioListRecyclerView.adapter = adapter
    binding.audioListRecyclerView.layoutManager = LinearLayoutManager(context)
    viewModel.audioList.observe(viewLifecycleOwner) { event ->
      when (event.status) {
        SUCCESS -> {
          val audioList = event.data ?: emptyList()
          adapter.refreshAudioList(audioList)
        }
        ERROR -> {

        }
        LOADING -> {

        }
      }
    }
  }

}