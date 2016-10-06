package com.riskpass.ofc

import kotlin.dom.addClass

class PreviewPanel(resultPanel: ResultPanel, errorPanel: ErrorPanel): Container(newEl()) {
  val leftPreviewImage = PreviewImage(resultPanel, errorPanel)
  val rightPreviewImage = PreviewImage(resultPanel, errorPanel)

  init {
    el.addClass("riskpass-ofc-preview_panel")
  }

  override fun onControlAdded() {
    add(leftPreviewImage)
    add(rightPreviewImage)
  }
}

