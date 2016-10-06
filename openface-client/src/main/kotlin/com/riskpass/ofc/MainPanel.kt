package com.riskpass.ofc

import kotlin.dom.addClass

class MainPanel(): Container(newEl()) {
  init {
    el.addClass("riskpass-ofc-main_panel")
  }

  override fun onControlAdded() {
    val errorPanel = ErrorPanel()
    val resultPanel = ResultPanel()
    val previewPanel = PreviewPanel(resultPanel, errorPanel)
    add(RequestPanel(resultPanel, previewPanel, errorPanel))
    add(errorPanel)
    add(previewPanel)
  }
}