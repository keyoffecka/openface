package com.riskpass.ofc

import kotlin.dom.addClass
import kotlin.dom.removeClass

class ErrorPanel: Container(newEl()) {
  var label: String get() = el.innerHTML ; set(value: String) {
    val v = escape(value)
    el.innerHTML = v
    if (v.isBlank()) {
      el.addClass("riskpass-ofc-hidden")
    } else {
      el.removeClass("riskpass-ofc-hidden")
    }
  }

  init {
    el.addClass("riskpass-ofc-error_panel", "riskpass-ofc-hidden")
  }
}