package com.adtarassov.audioplayer.ui.sreen.audiolist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.adtarassov.audioplayer.R
import com.adtarassov.audioplayer.data.AudioModel
import com.adtarassov.audioplayer.databinding.ItemAudioListBinding
import com.adtarassov.audioplayer.ui.sreen.audiolist.AudioListAdapter.AudioViewHolder

class AudioListAdapter(
  private val onAudioClickListener: (AudioModel) -> Unit,
  private val onAudioLikeClickListener: (AudioModel) -> Unit,
  private val onAudioProfileClick: (AudioModel) -> Unit,
) : RecyclerView.Adapter<AudioViewHolder>() {

  private var audioList = ArrayList<AudioModel>()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioViewHolder {
    val binding = ItemAudioListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return AudioViewHolder(binding, onAudioClickListener, onAudioLikeClickListener, onAudioProfileClick)
  }

  override fun onBindViewHolder(holder: AudioViewHolder, position: Int) {
    holder.bind(audioList[position])
  }

  override fun getItemCount(): Int = audioList.size

  fun refreshAudioList(list: List<AudioModel>) {
    audioList.clear()
    audioList.addAll(list)
    notifyDataSetChanged()
  }

  class AudioViewHolder(
    private val binding: ItemAudioListBinding,
    private val onAudioClickListener: (AudioModel) -> Unit,
    private val onAudioLikeClickListener: (AudioModel) -> Unit,
    private val onAudioProfileClick: (AudioModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
    fun bind(model: AudioModel) {
      binding.root.setOnClickListener {
        onAudioClickListener(model)
      }
      binding.apply {
        accountName.text = model.author
        accountName.setOnClickListener {
          onAudioProfileClick(model)
        }
        userAvatar.setOnClickListener {
          onAudioProfileClick(model)
        }
        title.text = model.title
        subtitle.text = model.subtitle
        likeImage.setOnClickListener {
          onAudioLikeClickListener(model)
        }
        val minutes = model.durationMs / 1000 / 60
        val seconds = model.durationMs / 1000 % 60
        if (model.isLiked) {
          val likeImageDrawable = AppCompatResources.getDrawable(binding.root.context, R.drawable.ic_heart)
          likeImageDrawable?.setTint(binding.root.context.getColor(R.color.red))
          likeImage.setImageDrawable(likeImageDrawable)
        } else {
          val likeImageDrawable = AppCompatResources.getDrawable(binding.root.context, R.drawable.ic_heart)
          likeImageDrawable?.setTint(binding.root.context.getColor(R.color.minorDisable))
          likeImage.setImageDrawable(likeImageDrawable)
        }
        audioDuration.text = "${"%02d".format(minutes)}:${"%02d".format(seconds)}"
      }
    }
  }

}