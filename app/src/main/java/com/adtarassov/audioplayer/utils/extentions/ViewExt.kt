package com.adtarassov.audioplayer.utils.extentions

import android.view.View

fun View.setGone() {
  if (visibility == View.GONE) return
  visibility = View.GONE
}

fun View.setInvisible() {
  if (visibility == View.INVISIBLE) return
  visibility = View.INVISIBLE
}

fun View.setVisible() {
  if (visibility == View.VISIBLE) return
  visibility = View.VISIBLE
}

var View.isVisible: Boolean
  get() = visibility == View.VISIBLE
  set(value) {
    if (value != isVisible) {
      visibility = if (value) View.VISIBLE else View.GONE
    }
  }