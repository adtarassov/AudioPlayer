package com.adtarassov.audioplayer.ui.sreen.audiolist

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.adtarassov.audioplayer.utils.extentions.toPx

class MarginItemDecoration(private val spaceSizeDp: Int) : RecyclerView.ItemDecoration() {
  override fun getItemOffsets(
    outRect: Rect, view: View,
    parent: RecyclerView,
    state: RecyclerView.State,
  ) {
    with(outRect) {
      if (parent.getChildAdapterPosition(view) == 0) {
        top = spaceSizeDp.toPx
      }
      bottom = spaceSizeDp.toPx
    }
  }
}